package com.gagik.scriptrunner.data

import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.domain.models.ScriptOutput
import com.gagik.scriptrunner.domain.repository.ScriptExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets


actual class ScriptExecutorImpl actual constructor() : ScriptExecutor {

    /**
     * Runs the code in a local subprocess.
     * @param code The code to run.
     * @param language The language of the code.
     */
    actual override fun execute(
        code: String,
        language: ScriptLanguage
    ): Flow<ScriptOutput> = flow {
        var process: Process? = null
        val tempFile = createTempFileForScript(code, language)

        try {
            val command = getCommandForLanguage(language, tempFile)

            val processBuilder = ProcessBuilder(command).apply {
                redirectErrorStream(true)
                directory(tempFile.parentFile)
            }

            process = processBuilder.start()

            process.inputStream.bufferedReader().use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    currentCoroutineContext().ensureActive()
                    emit(ScriptOutput.Line(line))
                    line = reader.readLine()
                }
            }

            val exitCode = process.waitFor()
            emit(ScriptOutput.Exit(exitCode))

        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: IOException) {
            emit(ScriptOutput.Error(e.message ?: "Unknown I/O Error"))
        } catch (e: Exception) {
            emit(ScriptOutput.Error("An unexpected error occurred: ${e.message}"))
        } finally {
            runCatching { tempFile.delete() }
            runCatching { process?.destroyForcibly() }
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Generates a unique temporary file to store the source code before execution.
     * Files are created in the system default temp directory.
     *
     * @param code The source code to be written to the temporary file.
     * @param language The programming language of the source code.
     */
    private fun createTempFileForScript(code: String, language: ScriptLanguage): File {
        val fileName = "script_${System.currentTimeMillis()}"
        return File.createTempFile(fileName, ".${language.extension}").apply {
            writeText(code, StandardCharsets.UTF_8)
        }
    }

    /**
     * Maps the [ScriptLanguage] to the appropriate system command.
     * * Note: Assumes 'kotlinc' or 'swift' is available in the system environment PATH.
     *
     * @param language The programming language of the source code.
     * @param scriptFile The temporary file containing the source code.
     */
    private fun getCommandForLanguage(language: ScriptLanguage, scriptFile: File): List<String> {
        return when (language) {
            ScriptLanguage.KOTLIN -> listOf("kotlinc", "-script", scriptFile.absolutePath)
            ScriptLanguage.SWIFT -> listOf("/usr/bin/env", "swift", scriptFile.absolutePath)
        }
    }
}
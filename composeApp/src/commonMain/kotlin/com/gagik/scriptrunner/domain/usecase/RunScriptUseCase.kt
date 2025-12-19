package com.gagik.scriptrunner.domain.usecase

import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.domain.models.ScriptOutput
import com.gagik.scriptrunner.domain.repository.ScriptExecutor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class RunScriptUseCase(private val scriptExecutor: ScriptExecutor) {
    // Matches compile-time errors: "filename:line:column:"
    private val compileErrorRegex = Regex("""^.*:(\d+):(\d+):""")
    
    // Matches runtime stack traces: "(filename.kts:3)" or "(filename.swift:3)"
    private val stackTraceRegex = Regex("""\(([^)]+\.(?:kts|swift)):(\d+)\)""")

    /**
     * Executes the given script code with the specified language.
     * @param code The script code to execute.
     * @param language The language of the script.
     *
     * @return A flow emitting the output of the script.
     **/
    operator fun invoke(code: String, language: ScriptLanguage): Flow<ScriptOutput> {
        if (code.isBlank()) {
            return flow {
                emit(ScriptOutput.Error("Script cannot be empty"))
                emit(ScriptOutput.Exit(1))
            }
        }

        return scriptExecutor.execute(code, language)
            .map(::mapOutputWithFileLocation)
    }


    /**
     * Maps the output to include the line number of the file.
     * Handles both compile-time errors and runtime stack traces.
     */
    private fun mapOutputWithFileLocation(output: ScriptOutput): ScriptOutput {
        if (output !is ScriptOutput.Line) return output

        // Try compile-time error format first: "filename:line:column:"
        compileErrorRegex.find(output.text)?.let { match ->
            return output.copy(
                linkRange = match.range,
                targetLineNumber = match.groupValues[1].toInt()
            )
        }

        // Try runtime stack trace format: "(filename.kts:3)"
        stackTraceRegex.find(output.text)?.let { match ->
            return output.copy(
                linkRange = match.range,
                targetLineNumber = match.groupValues[2].toInt()
            )
        }

        return output
    }
}
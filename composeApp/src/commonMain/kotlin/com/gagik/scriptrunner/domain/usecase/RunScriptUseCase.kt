package com.gagik.scriptrunner.domain.usecase

import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.domain.models.ScriptOutput
import com.gagik.scriptrunner.domain.repository.ScriptExecutor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class RunScriptUseCase(private val scriptExecutor: ScriptExecutor) {
    private val fileLocationRegex = Regex("""^.*:(\d+):(\d+):""")

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
     * maps the output to include the line number of the file.
     * @param output The output to map.
     *
     * @return The mapped output.
     */
    private fun mapOutputWithFileLocation(output: ScriptOutput): ScriptOutput {
        if (output !is ScriptOutput.Line) return output

        val match = fileLocationRegex.find(output.text) ?: return output

        return output.copy(
            linkRange = match.range,
            targetLineNumber = match.groupValues[1].toInt()
        )
    }
}
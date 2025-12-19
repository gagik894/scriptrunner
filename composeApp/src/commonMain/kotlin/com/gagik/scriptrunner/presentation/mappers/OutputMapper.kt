package com.gagik.scriptrunner.presentation.mappers

import com.gagik.scriptrunner.domain.models.ScriptOutput
import com.gagik.scriptrunner.presentation.models.ConsoleUiLine

/**
 * Maps domain [ScriptOutput] events to presentation-layer [ConsoleUiLine] models.
 *
 * This mapper is responsible for translating the raw output from script execution
 * into UI-ready console line models with appropriate styling information.
 */
class OutputMapper {

    /**
     * Converts a [ScriptOutput] to a [ConsoleUiLine] for display in the console.
     *
     * @param output The domain output event from script execution.
     * @return A UI-ready console line with type, text, and optional link information.
     */
    fun map(output: ScriptOutput): ConsoleUiLine {
        return when (output) {
            is ScriptOutput.Line -> ConsoleUiLine(
                text = output.text,
                type = if (output.isStdErr) ConsoleUiLine.Type.ERROR else ConsoleUiLine.Type.NORMAL,
                linkRange = output.linkRange,
                targetLineNumber = output.targetLineNumber
            )

            is ScriptOutput.Error -> ConsoleUiLine(
                text = output.message,
                type = ConsoleUiLine.Type.ERROR
            )

            is ScriptOutput.Exit -> ConsoleUiLine(
                text = "Process finished with exit code ${output.code}",
                type = ConsoleUiLine.Type.SYSTEM
            )
        }
    }
}

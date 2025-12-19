package com.gagik.scriptrunner.data

import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.domain.models.ScriptOutput
import com.gagik.scriptrunner.domain.repository.ScriptExecutor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Demo implementation for Web platforms.
 * Simulates script execution with realistic output since browsers can't run processes.
 */
actual class ScriptExecutorImpl actual constructor() : ScriptExecutor {
    actual override fun execute(
        code: String,
        language: ScriptLanguage
    ): Flow<ScriptOutput> = flow {
        // Simulate compilation/startup delay
        emit(ScriptOutput.Line("[Demo Mode] Running ${language.name} script...", isStdErr = false))
        delay(500)

        // Parse the code for print statements and simulate output
        val printStatements = extractPrintStatements(code, language)

        if (printStatements.isEmpty()) {
            emit(ScriptOutput.Line("No output detected in demo mode.", isStdErr = false))
        } else {
            for ((index, statement) in printStatements.withIndex()) {
                // Simulate execution time
                delay(300)
                emit(ScriptOutput.Line(statement, isStdErr = false))
            }
        }

        // Simulate potential error for demonstration
        if (code.contains("error", ignoreCase = true) || code.contains("throw", ignoreCase = true)) {
            delay(200)
            emit(
                ScriptOutput.Line(
                    "Script.kt:5: Error: Demo error simulation",
                    isStdErr = true
                )
            )
            emit(ScriptOutput.Exit(1))
        } else {
            delay(200)
            emit(ScriptOutput.Line("Script completed successfully!", isStdErr = false))
            emit(ScriptOutput.Exit(0))
        }
    }

    private fun extractPrintStatements(code: String, language: ScriptLanguage): List<String> {
        val results = mutableListOf<String>()

        // Match common print patterns
        val patterns = listOf(
            Regex("""println\s*\(\s*"([^"]*)"\s*\)"""),    // Kotlin: println("...")
            Regex("""print\s*\(\s*"([^"]*)"\s*\)"""),      // Kotlin: print("...")
            Regex("""print\(\s*"([^"]*)"\s*\)"""),         // Swift: print("...")
            Regex("""print\(\s*'([^']*)'\s*\)""")          // Swift: print('...')
        )

        for (pattern in patterns) {
            pattern.findAll(code).forEach { match ->
                match.groupValues.getOrNull(1)?.let { results.add(it) }
            }
        }

        // If no patterns matched, provide demo output based on language
        if (results.isEmpty()) {
            results.add(getDemoOutput(language))
        }

        return results
    }

    private fun getDemoOutput(language: ScriptLanguage): String = when (language) {
        ScriptLanguage.KOTLIN -> "Hello from Kotlin!"
        ScriptLanguage.SWIFT -> "Hello from Swift!"
    }
}
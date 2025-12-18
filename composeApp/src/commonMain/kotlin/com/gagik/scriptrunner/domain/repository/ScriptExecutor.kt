package com.gagik.scriptrunner.domain.repository

import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.domain.models.ScriptOutput
import kotlinx.coroutines.flow.Flow

interface ScriptExecutor {
    /**
     * Executes the [code] in the specified [language].
     *
     * @param code The source code to be executed.
     * @param language The programming language environment to use.
     * @return A cold [Flow] emitting [ScriptOutput] events. The flow completes
     * when the subprocess terminates or is cancelled.
     */
    fun execute(code: String, language: ScriptLanguage): Flow<ScriptOutput>
}
package com.gagik.scriptrunner.domain.usecase

import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.domain.models.ScriptOutput
import com.gagik.scriptrunner.domain.repository.ScriptExecutor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RunScriptUseCase(private val scriptExecutor: ScriptExecutor) {
    /**
     * Validates and initiates script execution.
     *
     * @throws kotlin.coroutines.cancellation.CancellationException if the underlying coroutine scope is cancelled.
     */
    operator fun invoke(code: String, language: ScriptLanguage): Flow<ScriptOutput> {

        //execute only non blanc codes
        if (code.isBlank()) {
            return flow {
                emit(ScriptOutput.Error("Script cannot be empty"))
                emit(ScriptOutput.Exit(1))
            }
        }

        return scriptExecutor.execute(code, language)
    }
}
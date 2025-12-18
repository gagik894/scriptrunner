package com.gagik.scriptrunner.data

import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.domain.models.ScriptOutput
import com.gagik.scriptrunner.domain.repository.ScriptExecutor
import kotlinx.coroutines.flow.Flow

expect class ScriptExecutorImpl() : ScriptExecutor {
    override fun execute(code: String, language: ScriptLanguage): Flow<ScriptOutput>
}
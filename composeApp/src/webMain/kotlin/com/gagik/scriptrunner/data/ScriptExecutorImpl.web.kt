package com.gagik.scriptrunner.data

import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.domain.models.ScriptOutput
import com.gagik.scriptrunner.domain.repository.ScriptExecutor
import kotlinx.coroutines.flow.Flow

actual class ScriptExecutorImpl actual constructor() :
    ScriptExecutor {
    actual override fun execute(
        code: String,
        language: ScriptLanguage
    ): Flow<ScriptOutput> {
        TODO("Not yet implemented")
    }
}
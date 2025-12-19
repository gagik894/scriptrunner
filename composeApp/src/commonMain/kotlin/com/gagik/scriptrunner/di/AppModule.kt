package com.gagik.scriptrunner.di

import com.gagik.scriptrunner.data.ScriptExecutorImpl
import com.gagik.scriptrunner.domain.repository.ScriptExecutor
import com.gagik.scriptrunner.domain.usecase.RunScriptUseCase
import com.gagik.scriptrunner.presentation.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ScriptExecutor> { ScriptExecutorImpl() }
    single { RunScriptUseCase(get()) }
    viewModel { MainViewModel(get()) }
}

package com.gagik.scriptrunner.domain.usecase

import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.domain.models.ScriptOutput
import com.gagik.scriptrunner.domain.repository.ScriptExecutor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class RunScriptUseCaseTest {

    @Test
    fun emptyCodeReturnsErrorAndExit() = runTest {
        val useCase = RunScriptUseCase(FakeScriptExecutor())

        val outputs = useCase("", ScriptLanguage.KOTLIN).toList()

        assertEquals(2, outputs.size)
        assertIs<ScriptOutput.Error>(outputs[0])
        assertEquals("Script cannot be empty", (outputs[0] as ScriptOutput.Error).message)
        assertIs<ScriptOutput.Exit>(outputs[1])
        assertEquals(1, (outputs[1] as ScriptOutput.Exit).code)
    }

    @Test
    fun blankCodeReturnsErrorAndExit() = runTest {
        val useCase = RunScriptUseCase(FakeScriptExecutor())

        val outputs = useCase("   \n\t  ", ScriptLanguage.KOTLIN).toList()

        assertEquals(2, outputs.size)
        assertIs<ScriptOutput.Error>(outputs[0])
        assertIs<ScriptOutput.Exit>(outputs[1])
    }

    // endregion

    // region Compile Error Regex Tests

    @Test
    fun compileErrorLineIsParsed() = runTest {
        val errorLine = "script.kts:5:10: error: unresolved reference 'foo'"
        val executor = FakeScriptExecutor(
            listOf(ScriptOutput.Line(errorLine, isStdErr = true))
        )
        val useCase = RunScriptUseCase(executor)

        val outputs = useCase("println(foo)", ScriptLanguage.KOTLIN).toList()

        assertEquals(1, outputs.size)
        val output = outputs[0]
        assertIs<ScriptOutput.Line>(output)
        assertEquals(errorLine, output.text)
        // "script.kts:5:10:" is 16 characters (indices 0..15)
        assertEquals(0..15, output.linkRange)
        assertEquals(5, output.targetLineNumber)
    }

    @Test
    fun compileErrorWithDifferentLineNumbers() = runTest {
        val errorLine = "main.kts:123:45: error: something wrong"
        val executor = FakeScriptExecutor(
            listOf(ScriptOutput.Line(errorLine, isStdErr = true))
        )
        val useCase = RunScriptUseCase(executor)

        val outputs = useCase("code", ScriptLanguage.KOTLIN).toList()

        val output = outputs[0] as ScriptOutput.Line
        assertEquals(123, output.targetLineNumber)
    }

    // endregion

    // region Stack Trace Regex Tests

    @Test
    fun kotlinStackTraceLineIsParsed() = runTest {
        val stackLine = "    at Script.main(script.kts:3)"
        val executor = FakeScriptExecutor(
            listOf(ScriptOutput.Line(stackLine, isStdErr = true))
        )
        val useCase = RunScriptUseCase(executor)

        val outputs = useCase("throw Exception()", ScriptLanguage.KOTLIN).toList()

        val output = outputs[0] as ScriptOutput.Line
        assertEquals(stackLine, output.text)
        assertEquals(3, output.targetLineNumber)
        // "(script.kts:3)" starts at index 18, is 14 chars (indices 18..31)
        assertEquals(18..31, output.linkRange)
    }

    @Test
    fun swiftStackTraceLineIsParsed() = runTest {
        val stackLine = "    at main(script.swift:42)"
        val executor = FakeScriptExecutor(
            listOf(ScriptOutput.Line(stackLine, isStdErr = true))
        )
        val useCase = RunScriptUseCase(executor)

        val outputs = useCase("fatalError()", ScriptLanguage.SWIFT).toList()

        val output = outputs[0] as ScriptOutput.Line
        assertEquals(42, output.targetLineNumber)
    }

    // endregion

    // region Passthrough Tests

    @Test
    fun normalLinePassesThroughUnchanged() = runTest {
        val normalLine = "Hello from Kotlin!"
        val executor = FakeScriptExecutor(
            listOf(ScriptOutput.Line(normalLine, isStdErr = false))
        )
        val useCase = RunScriptUseCase(executor)

        val outputs = useCase("println(\"test\")", ScriptLanguage.KOTLIN).toList()

        val output = outputs[0] as ScriptOutput.Line
        assertEquals(normalLine, output.text)
        assertNull(output.linkRange)
        assertNull(output.targetLineNumber)
    }

    @Test
    fun exitOutputPassesThrough() = runTest {
        val executor = FakeScriptExecutor(
            listOf(ScriptOutput.Exit(0))
        )
        val useCase = RunScriptUseCase(executor)

        val outputs = useCase("println(\"done\")", ScriptLanguage.KOTLIN).toList()

        assertEquals(1, outputs.size)
        assertIs<ScriptOutput.Exit>(outputs[0])
        assertEquals(0, (outputs[0] as ScriptOutput.Exit).code)
    }

    @Test
    fun errorOutputPassesThrough() = runTest {
        val executor = FakeScriptExecutor(
            listOf(ScriptOutput.Error("Process failed"))
        )
        val useCase = RunScriptUseCase(executor)

        val outputs = useCase("code", ScriptLanguage.KOTLIN).toList()

        assertEquals(1, outputs.size)
        assertIs<ScriptOutput.Error>(outputs[0])
        assertEquals("Process failed", (outputs[0] as ScriptOutput.Error).message)
    }

    private class FakeScriptExecutor(
        private val outputs: List<ScriptOutput> = emptyList()
    ) : ScriptExecutor {
        override fun execute(code: String, language: ScriptLanguage): Flow<ScriptOutput> = flow {
            outputs.forEach { emit(it) }
        }
    }
}

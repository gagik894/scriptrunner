package com.gagik.scriptrunner.presentation.mappers

import com.gagik.scriptrunner.domain.models.ScriptOutput
import com.gagik.scriptrunner.presentation.models.ConsoleUiLine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OutputMapperTest {

    private val mapper = OutputMapper()

    @Test
    fun lineStdoutMapsToNormalType() {
        val output = ScriptOutput.Line(text = "Hello World", isStdErr = false)

        val result = mapper.map(output)

        assertEquals("Hello World", result.text)
        assertEquals(ConsoleUiLine.Type.NORMAL, result.type)
    }

    @Test
    fun lineStderrMapsToErrorType() {
        val output = ScriptOutput.Line(text = "Error occurred", isStdErr = true)

        val result = mapper.map(output)

        assertEquals("Error occurred", result.text)
        assertEquals(ConsoleUiLine.Type.ERROR, result.type)
    }

    @Test
    fun lineLinkRangeIsPreserved() {
        val linkRange = 5..15
        val output = ScriptOutput.Line(
            text = "at (script.kts:3)",
            isStdErr = true,
            linkRange = linkRange,
            targetLineNumber = 3
        )

        val result = mapper.map(output)

        assertEquals(linkRange, result.linkRange)
        assertEquals(3, result.targetLineNumber)
    }

    @Test
    fun lineWithoutLinkHasNullLinkProperties() {
        val output = ScriptOutput.Line(text = "Normal output", isStdErr = false)

        val result = mapper.map(output)

        assertNull(result.linkRange)
        assertNull(result.targetLineNumber)
    }

    @Test
    fun errorMapsToErrorType() {
        val output = ScriptOutput.Error(message = "Script execution failed")

        val result = mapper.map(output)

        assertEquals("Script execution failed", result.text)
        assertEquals(ConsoleUiLine.Type.ERROR, result.type)
    }

    @Test
    fun errorHasNoLinkProperties() {
        val output = ScriptOutput.Error(message = "Error")

        val result = mapper.map(output)

        assertNull(result.linkRange)
        assertNull(result.targetLineNumber)
    }

    @Test
    fun exitMapsToSystemType() {
        val output = ScriptOutput.Exit(code = 0)

        val result = mapper.map(output)

        assertEquals(ConsoleUiLine.Type.SYSTEM, result.type)
    }

    @Test
    fun exitCodeZeroFormatsCorrectly() {
        val output = ScriptOutput.Exit(code = 0)

        val result = mapper.map(output)

        assertEquals("Process finished with exit code 0", result.text)
    }

    @Test
    fun exitCodeNonZeroFormatsCorrectly() {
        val output = ScriptOutput.Exit(code = 1)

        val result = mapper.map(output)

        assertEquals("Process finished with exit code 1", result.text)
    }

    @Test
    fun exitCodeNegativeFormatsCorrectly() {
        val output = ScriptOutput.Exit(code = -1)

        val result = mapper.map(output)

        assertEquals("Process finished with exit code -1", result.text)
    }

}

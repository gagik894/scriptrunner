package com.gagik.scriptrunner.domain.provider

import com.gagik.scriptrunner.domain.models.ScriptLanguage
import kotlin.test.Test
import kotlin.test.assertTrue

class ScriptTemplateProviderTest {

    @Test
    fun kotlinTemplateContainsPrintln() {
        val template = ScriptTemplateProvider.getTemplate(ScriptLanguage.KOTLIN)

        assertTrue(template.contains("println"), "Kotlin template should use println")
        assertTrue(template.contains("Hello"), "Kotlin template should have a greeting")
    }

    @Test
    fun kotlinTemplateContainsThreadSleep() {
        val template = ScriptTemplateProvider.getTemplate(ScriptLanguage.KOTLIN)

        assertTrue(template.contains("Thread.sleep"), "Kotlin template should demonstrate sleep")
    }

    @Test
    fun swiftTemplateContainsPrint() {
        val template = ScriptTemplateProvider.getTemplate(ScriptLanguage.SWIFT)

        assertTrue(template.contains("print("), "Swift template should use print")
        assertTrue(template.contains("Hello"), "Swift template should have a greeting")
    }

    @Test
    fun swiftTemplateContainsFoundationImport() {
        val template = ScriptTemplateProvider.getTemplate(ScriptLanguage.SWIFT)

        assertTrue(template.contains("import Foundation"), "Swift template should import Foundation")
    }

    @Test
    fun swiftTemplateContainsThreadSleep() {
        val template = ScriptTemplateProvider.getTemplate(ScriptLanguage.SWIFT)

        assertTrue(
            template.contains("Thread.sleep(forTimeInterval"),
            "Swift template should demonstrate sleep"
        )
    }

    @Test
    fun templatesAreNotBlank() {
        ScriptLanguage.entries.forEach { language ->
            val template = ScriptTemplateProvider.getTemplate(language)
            assertTrue(template.isNotBlank(), "Template for $language should not be blank")
        }
    }
}

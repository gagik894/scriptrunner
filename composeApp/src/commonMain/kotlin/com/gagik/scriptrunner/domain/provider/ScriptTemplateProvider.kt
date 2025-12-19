package com.gagik.scriptrunner.domain.provider

import com.gagik.scriptrunner.domain.models.ScriptLanguage

/**
 * Provides default code templates for each supported [ScriptLanguage].
 *
 * Templates are used when initializing the editor or when switching languages,
 * giving users a starting point for their scripts.
 */
object ScriptTemplateProvider {

    /**
     * Returns the default code template for the given [language].
     */
    fun getTemplate(language: ScriptLanguage): String {
        return when (language) {
            ScriptLanguage.KOTLIN -> """
                // Kotlin Script - top-level code runs automatically
                println("Hello from Kotlin!")
                Thread.sleep(1000)
                println("Done.")
            """.trimIndent()

            ScriptLanguage.SWIFT -> """
                import Foundation
                
                print("Hello from Swift!")
                Thread.sleep(forTimeInterval: 1.0)
                print("Done.")
            """.trimIndent()
        }
    }
}

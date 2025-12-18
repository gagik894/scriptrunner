package com.gagik.scriptrunner.domain.models


/**
 * Represents the supported scripting languages.
 *
 * Each language has a file extension and a display name associated with it.
 *
 * @property extension The file extension for scripts of this language (e.g., "kts" for Kotlin).
 * @property displayName The user-friendly name of the language (e.g., "Kotlin").
 */
enum class ScriptLanguage(val extension: String, val displayName: String) {
    KOTLIN("kts", "Kotlin"),
    SWIFT("swift", "Swift")
}
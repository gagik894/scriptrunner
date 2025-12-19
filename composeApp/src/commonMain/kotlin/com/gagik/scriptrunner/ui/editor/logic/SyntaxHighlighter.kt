package com.gagik.scriptrunner.ui.editor.logic

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.gagik.scriptrunner.domain.models.ScriptLanguage

class CodeSyntaxHighlighter(
    language: ScriptLanguage
) : VisualTransformation {

    private val keywords = when (language) {
        ScriptLanguage.KOTLIN -> listOf(
            "fun", "val", "var", "class", "interface", "return", "if", "else",
            "for", "while", "package", "import", "true", "false", "null", "this"
        )

        ScriptLanguage.SWIFT -> listOf(
            "func", "let", "var", "class", "protocol", "return", "if", "else",
            "for", "while", "import", "struct", "true", "false", "nil", "self"
        )
    }

    private val styles = mapOf(
        "KEYWORD" to SpanStyle(color = Color(0xFFBB633B), fontWeight = FontWeight.Bold),
        "STRING" to SpanStyle(color = Color(0xFF1E8200)),
        "COMMENT" to SpanStyle(color = Color(0xFF808080), fontStyle = FontStyle.Italic),
        "NUMBER" to SpanStyle(color = Color(0xFF68BBAF)),
        "ANNOTATION" to SpanStyle(color = Color(0xFF59D03E)),
        "FUNCTION" to SpanStyle(color = Color(0xFFFFC66D)),
        "VARIABLE" to SpanStyle(color = Color(0xFF4C4AE7))
    )

    private val regex = Regex(
        // Strings (matches "...")
        "(\".*?\")" +
                "|" +
                // Comments (matches //...)
                "(//.*)" +
                "|" +
                // Keywords (matches whole words only)
                "\\b(${keywords.joinToString("|")})\\b" +
                "|" +
                // Numbers (matches digits)
                "\\b(\\d+)\\b" +
                "|" +
                //Annotations (matches @Word)
                "(@\\w+)" +
                "|" +
                //Function names (matches Word() )
                "\\b([a-zA-Z_]\\w*)(?=\\s*\\()" +
                "|" +
                //Variables/Identifiers (matches any other word)
                "\\b([a-zA-Z_]\\w*)\\b"
    )

    override fun filter(text: AnnotatedString): TransformedText {
        val rawText = text.text
        if (rawText.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        val builder = AnnotatedString.Builder(text)

        regex.findAll(rawText).forEach { matchResult ->
            val range = matchResult.range
            val start = range.first
            val end = range.last + 1

            when {
                matchResult.groups[1] != null -> builder.addStyle(styles["STRING"]!!, start, end)
                matchResult.groups[2] != null -> builder.addStyle(styles["COMMENT"]!!, start, end)
                matchResult.groups[3] != null -> builder.addStyle(styles["KEYWORD"]!!, start, end)
                matchResult.groups[4] != null -> builder.addStyle(styles["NUMBER"]!!, start, end)
                matchResult.groups[5] != null -> builder.addStyle(
                    styles["ANNOTATION"]!!,
                    start,
                    end
                )

                matchResult.groups[6] != null -> builder.addStyle(styles["FUNCTION"]!!, start, end)
                matchResult.groups[7] != null -> builder.addStyle(styles["VARIABLE"]!!, start, end)
            }
        }

        return TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
    }
}
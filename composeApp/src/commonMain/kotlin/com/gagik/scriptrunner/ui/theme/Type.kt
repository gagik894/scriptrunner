package com.gagik.scriptrunner.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import scriptrunner.composeapp.generated.resources.JetBrainsMono_VariableFont_wght
import scriptrunner.composeapp.generated.resources.Res

val AppTypography = Typography()
@Composable
fun getJetBrainsMonoFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.JetBrainsMono_VariableFont_wght, FontWeight.Light),
        Font(Res.font.JetBrainsMono_VariableFont_wght, FontWeight.Normal),
        Font(Res.font.JetBrainsMono_VariableFont_wght, FontWeight.Medium),
        Font(Res.font.JetBrainsMono_VariableFont_wght, FontWeight.SemiBold),
        Font(Res.font.JetBrainsMono_VariableFont_wght, FontWeight.Bold)
    )
}
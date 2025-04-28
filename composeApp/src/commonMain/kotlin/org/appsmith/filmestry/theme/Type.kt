package org.appsmith.filmestry.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import filmestry.composeapp.generated.resources.Res
import filmestry.composeapp.generated.resources.dm_serif
import filmestry.composeapp.generated.resources.nunito_sans
import org.jetbrains.compose.resources.Font

@Composable
fun NunitoSansFamily() = FontFamily(
    Font(Res.font.nunito_sans, weight = FontWeight.Normal),
)

@Composable
fun DMSerifFamily() = FontFamily(
    Font(Res.font.dm_serif, weight = FontWeight.Normal),
)


@Composable
fun AppTypography() = Typography().run {

    val nunitoSansFamily = NunitoSansFamily()
    val dmSerifFamily = DMSerifFamily()

    copy(
        displayLarge = displayLarge.copy(fontFamily = dmSerifFamily),
        displayMedium = displayMedium.copy(fontFamily = dmSerifFamily),
        displaySmall = displaySmall.copy(fontFamily = dmSerifFamily),
        headlineLarge = headlineLarge.copy(fontFamily = dmSerifFamily),
        headlineMedium = headlineMedium.copy(fontFamily = dmSerifFamily),
        headlineSmall = headlineSmall.copy(fontFamily = dmSerifFamily),
        titleLarge = titleLarge.copy(fontFamily = dmSerifFamily),
        titleMedium = titleMedium.copy(fontFamily = dmSerifFamily),
        titleSmall = titleSmall.copy(fontFamily = dmSerifFamily),
        bodyLarge = bodyLarge.copy(fontFamily = nunitoSansFamily),
        bodyMedium = bodyMedium.copy(fontFamily = nunitoSansFamily),
        bodySmall = bodySmall.copy(fontFamily = nunitoSansFamily),
        labelLarge = labelLarge.copy(fontFamily = nunitoSansFamily),
        labelMedium = labelMedium.copy(fontFamily = nunitoSansFamily),
        labelSmall = labelSmall.copy(fontFamily = nunitoSansFamily)
    )
}


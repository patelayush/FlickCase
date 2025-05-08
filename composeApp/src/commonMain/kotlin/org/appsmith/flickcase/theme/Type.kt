package org.appsmith.flickcase.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import flickcase.composeapp.generated.resources.Res
import flickcase.composeapp.generated.resources.dm_serif
import flickcase.composeapp.generated.resources.nunito_sans
import flickcase.composeapp.generated.resources.roboto_slab
import org.jetbrains.compose.resources.Font

@Composable
fun RobotoSlabFamily() = FontFamily(
    Font(Res.font.roboto_slab, weight = FontWeight.Normal),
)

@Composable
fun NunitoSansFamily() = FontFamily(
    Font(Res.font.nunito_sans, weight = FontWeight.Normal),
)

@Composable
fun AppTypography() = Typography().run {

    val nunitoSansFamily = NunitoSansFamily()
    val robotoSlabFamily = RobotoSlabFamily()

    copy(
        displayLarge = displayLarge.copy(fontFamily = robotoSlabFamily, letterSpacing = 1.5.sp),
        displayMedium = displayMedium.copy(fontFamily = robotoSlabFamily, letterSpacing = 1.5.sp),
        displaySmall = displaySmall.copy(fontFamily = robotoSlabFamily, letterSpacing = 1.5.sp),
        headlineLarge = headlineLarge.copy(fontFamily = robotoSlabFamily, letterSpacing = 1.5.sp),
        headlineMedium = headlineMedium.copy(fontFamily = robotoSlabFamily, letterSpacing = 1.5.sp),
        headlineSmall = headlineSmall.copy(fontFamily = robotoSlabFamily, letterSpacing = 1.5.sp),
        titleLarge = titleLarge.copy(fontFamily = robotoSlabFamily, letterSpacing = 1.5.sp),
        titleMedium = titleMedium.copy(fontFamily = robotoSlabFamily, letterSpacing = 1.5.sp),
        titleSmall = titleSmall.copy(fontFamily = robotoSlabFamily, letterSpacing = 1.5.sp),
        bodyLarge = bodyLarge.copy(fontFamily = nunitoSansFamily),
        bodyMedium = bodyMedium.copy(fontFamily = nunitoSansFamily),
        bodySmall = bodySmall.copy(fontFamily = nunitoSansFamily),
        labelLarge = labelLarge.copy(fontFamily = nunitoSansFamily),
        labelMedium = labelMedium.copy(fontFamily = nunitoSansFamily),
        labelSmall = labelSmall.copy(fontFamily = nunitoSansFamily)
    )
}


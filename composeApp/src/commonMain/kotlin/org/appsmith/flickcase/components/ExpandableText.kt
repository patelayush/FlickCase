package org.appsmith.flickcase.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    color:Color,
    style:TextStyle,
    showMoreText: String = "Read More",
    showLessText: String = "Read Less"
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showReadMoreButton by remember { mutableStateOf(false) }

    Column(
        modifier.animateContentSize()
    ) {
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            color = color,
            style = style,
            onTextLayout = { result ->
                showReadMoreButton = result.lineCount > 2 && result.isLineEllipsized(2)
            },
        )

        if (showReadMoreButton) {
            Text(
                text = if (isExpanded) showLessText else showMoreText,
                modifier = Modifier.clickable { isExpanded = !isExpanded },
                style = style,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}
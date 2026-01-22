package io.github.kingg22.vibrion.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.Icons
import io.github.kingg22.vibrion.filled.KeyboardArrowDown
import io.github.kingg22.vibrion.filled.KeyboardArrowRight
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme

@Composable
fun ExpandableItem(
    label: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    content: @Composable (ColumnScope.() -> Unit),
) {
    var expanded by rememberSaveable { mutableStateOf(true) }

    Column(modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth().clickable { expanded = !expanded }.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                if (expanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                contentDescription = if (expanded) "collapse" else "expand",
            )
            Spacer(Modifier.width(8.dp))
            Text(label, style = style)
        }
        AnimatedVisibility(visible = expanded) { Column { content() } }
    }
}

@Composable
@Preview
private fun ExpandableItemPreview() {
    VibrionAppTheme {
        ExpandableItem("Expandable Item") {
            Text("Content of the expandable item")
        }
    }
}

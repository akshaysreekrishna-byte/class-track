package com.classtrack.feature.subjects.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.classtrack.core.domain.model.SubjectType

private val PrimaryFixed = Color(0xFFEADDFF)
private val OnPrimaryFixed = Color(0xFF21005D)
private val SecondaryContainer = Color(0xFFE8DEF8)
private val OnSecondaryContainer = Color(0xFF1D192B)

/**
 * Pill-shaped chip displaying Theory or Lab subject type.
 * Colors match the Stitch card chip tokens exactly.
 */
@Composable
fun SubjectTypeChip(
    type: SubjectType,
    modifier: Modifier = Modifier,
) {
    val (bg, fg, label) = type.toChipTokens()
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(percent = 50),
        color = bg,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = fg,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

private data class ChipTokens(val bg: Color, val fg: Color, val label: String)

private fun SubjectType.toChipTokens(): ChipTokens = when (this) {
    SubjectType.THEORY -> ChipTokens(PrimaryFixed, OnPrimaryFixed, "Theory")
    SubjectType.LAB -> ChipTokens(SecondaryContainer, OnSecondaryContainer, "Lab")
}

@Preview(showBackground = true, backgroundColor = 0xFFFDF8FF)
@Composable
private fun SubjectTypeChipPreview() {
    MaterialTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
        ) {
            SubjectTypeChip(type = SubjectType.THEORY)
            SubjectTypeChip(type = SubjectType.LAB)
        }
    }
}

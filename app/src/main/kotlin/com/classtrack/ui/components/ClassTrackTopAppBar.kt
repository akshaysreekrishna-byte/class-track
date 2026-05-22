package com.classtrack.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.classtrack.ui.theme.LexendFontFamily
import com.classtrack.ui.theme.PrimaryContainer

/**
 * App-wide top bar: circular initials avatar + "Class Tracker" brand title.
 * Matches the sticky header from Stitch designs across all three screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassTrackTopAppBar(
    initials: String = "CT",
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        title = {
            Text(
                text = "Class Tracker",
                fontFamily = LexendFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
            )
        },
        navigationIcon = {
            AvatarBadge(initials = initials)
        },
    )
}

@Composable
private fun AvatarBadge(initials: String) {
    Surface(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape),
        color = PrimaryContainer,
        shape = CircleShape,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = initials,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

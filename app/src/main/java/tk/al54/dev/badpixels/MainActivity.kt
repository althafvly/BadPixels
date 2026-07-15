package tk.al54.dev.badpixels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BadPixelsScreen(
                versionName = BuildConfig.VERSION_NAME,
                versionCode = BuildConfig.VERSION_CODE
            )
        }
    }
}

private val COLORS = listOf(
    Color.Black, Color.Red, Color.Green, Color.Blue,
    Color.Cyan, Color.Magenta, Color.Yellow, Color.White
)

@Composable
fun BadPixelsScreen(versionName: String, versionCode: Int) {
    var colorIndex by remember { mutableIntStateOf(0) }
    var showInfo by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(COLORS[colorIndex])
            .clickable {
                colorIndex = (colorIndex + 1) % COLORS.size
                showInfo = false
            }
            .pointerInput(Unit) {
                var dragX = 0f
                var dragY = 0f
                detectDragGestures(
                    onDragStart = { dragX = 0f; dragY = 0f },
                    onDrag = { change, amount ->
                        change.consume()
                        dragX += amount.x
                        dragY += amount.y
                    },
                    onDragEnd = {
                        val minDistance = 120f
                        val absX = abs(dragX)
                        val absY = abs(dragY)
                        val direction = if (absX > absY) {
                            if (absX > minDistance) if (dragX < 0) 1 else -1 else 0
                        } else {
                            if (absY > minDistance) if (dragY < 0) 1 else -1 else 0
                        }
                        if (direction != 0) {
                            colorIndex = (colorIndex + direction + COLORS.size) % COLORS.size
                            showInfo = false
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (showInfo) {
            InfoOverlay(versionName, versionCode)
        }
    }
}

@Composable
fun InfoOverlay(versionName: String, versionCode: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 30.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = stringResource(R.string.info),
            fontSize = 18.sp,
            color = Color.White,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = stringResource(
                R.string.version_display,
                stringResource(R.string.version),
                versionName,
                stringResource(R.string.build),
                versionCode
            ),
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

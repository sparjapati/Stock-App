package com.localCachingWithJetpackCompose.presentation.companyInfoScreen

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localCachingWithJetpackCompose.domain.models.IntraDayInfo
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun StockChart(
    modifier: Modifier = Modifier,
    infos: List<IntraDayInfo> = emptyList(),
    graphColor: Color = Color.Green
) {
    Log.d("abcd", "StockChart: ${infos.size}")
    val spacing = 100f
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.1f)
    }
    val upperValue = remember {
        infos.maxOf { it.close }.plus(1).roundToInt() ?: 0
    }
    val lowerValue = remember {
        infos.minOf { it.close }.roundToInt() ?: 0
    }
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(modifier = modifier) {
        val spacePerHour = (size.width - spacing) / infos.size
        (infos.indices step 2).forEach { i ->
            val item = infos[i]
            drawContext.canvas.nativeCanvas.drawText(
                "${item.dateTime.hour}",
                spacing + i * spacePerHour,
                size.height - 20,
                textPaint
            )
        }
        val priceSteps = (upperValue - lowerValue) / 5f
        (0..4).forEach { i ->
            drawContext.canvas.nativeCanvas.drawText(
                (round(lowerValue + i * priceSteps * 10) / 10).toString(),
                30f,
                size.height - spacing - i * size.height / 5f,
                textPaint
            )
        }
        var lastX = 0f
        val strokePath = Path().apply {
            val height = size.height
            for (i in infos.indices) {
                val info = infos[i]
                val nextInfo = infos.getOrNull(i + 1) ?: infos.last()
                val leftRatio = (info.close - lowerValue) / (upperValue - lowerValue)
                val rightRatio = (nextInfo.close - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (leftRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (rightRatio * height).toFloat()
                lastX = (x1 + x2) / 2f
                if (i == 0)
                    moveTo(x1, y1)
                quadraticBezierTo(x1, y1, lastX, (y1 + y2) / 2f)
            }
        }

        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
            lineTo(lastX, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    graphColor,
                    transparentGraphColor
                ),
                endY = size.height - spacing
            )
        )
        drawPath(
            path = strokePath,
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}
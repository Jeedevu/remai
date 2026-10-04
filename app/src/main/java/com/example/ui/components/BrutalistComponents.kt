package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Neo-brutalist container with hard offset shadow and solid ink border.
 */
@Composable
fun HardShadowCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = RemSurfaceContainerLowest,
    borderColor: Color = RemInkBlack,
    borderWidth: Dp = 2.5.dp,
    shadowOffset: Dp = 3.dp,
    shadowColor: Color = RemInkBlack,
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = PaddingValues(12.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedOffset by animateDpAsState(
        targetValue = if (isPressed && onClick != null) 1.dp else shadowOffset,
        label = "shadow_offset"
    )

    Box(
        modifier = modifier
    ) {
        // Hard shadow layer behind
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = animatedOffset, y = animatedOffset)
                .background(shadowColor, shape)
        )

        // Main card layer on top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, shape)
                .border(borderWidth, borderColor, shape)
                .clip(shape)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick
                        )
                    } else Modifier
                )
                .padding(contentPadding),
            content = content
        )
    }
}

/**
 * Chunky tactile button with full-bleed hard shadow that sinks when pressed.
 */
@Composable
fun StompButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = RemPrimaryContainer,
    contentColor: Color = RemOnSurface,
    borderColor: Color = RemInkBlack,
    borderWidth: Dp = 2.5.dp,
    shadowOffset: Dp = 4.dp,
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val currentOffset by animateDpAsState(
        targetValue = if (isPressed) 1.dp else shadowOffset,
        label = "btn_press"
    )

    Box(modifier = modifier) {
        // Shadow plate
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = currentOffset, y = currentOffset)
                .background(RemInkBlack, shape)
        )

        // Button plate
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, shape)
                .border(borderWidth, borderColor, shape)
                .clip(shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            content = content
        )
    }
}

/**
 * Segmented progress bar with diagonal hazard stripes on completed segments.
 */
@Composable
fun HazardSegmentedProgressBar(
    completedSegments: Int,
    totalSegments: Int = 5,
    modifier: Modifier = Modifier,
    barHeight: Dp = 18.dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for (i in 0 until totalSegments) {
            val isCompleted = i < completedSegments
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (isCompleted) RemPrimaryContainer else RemSurfaceContainerHighest,
                        RoundedCornerShape(4.dp)
                    )
                    .border(1.dp, RemInkBlack.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp))
            ) {
                if (isCompleted) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stripeWidth = 6.dp.toPx()
                        val step = stripeWidth * 2
                        var x = -size.height
                        while (x < size.width + size.height) {
                            drawLine(
                                color = RemInkBlack.copy(alpha = 0.15f),
                                start = Offset(x, 0f),
                                end = Offset(x + size.height, size.height),
                                strokeWidth = stripeWidth
                            )
                            x += step
                        }
                    }
                }
            }
        }
    }
}

/**
 * Diagonal hazard bar filling a container (for splash screen progress).
 */
@Composable
fun DiagonalHazardBar(
    progressPercent: Float, // 0f .. 1f
    modifier: Modifier = Modifier,
    height: Dp = 26.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(RemSurfaceContainerLowest, RoundedCornerShape(12.dp))
            .border(2.5.dp, RemInkBlack, RoundedCornerShape(12.dp))
            .padding(3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = progressPercent.coerceIn(0f, 1f))
                .clip(RoundedCornerShape(8.dp))
                .background(RemPrimaryContainer)
                .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stripeWidth = 8.dp.toPx()
                val step = stripeWidth * 2.2f
                var x = -size.height * 2
                while (x < size.width + size.height) {
                    val path = Path().apply {
                        moveTo(x, size.height)
                        lineTo(x + size.height, 0f)
                        lineTo(x + size.height + stripeWidth, 0f)
                        lineTo(x + stripeWidth, size.height)
                        close()
                    }
                    drawPath(path, color = RemInkBlack)
                    x += step
                }
            }
        }
    }
}

/**
 * Retro Halftone / Dot Matrix graphic.
 */
@Composable
fun HalftoneDotMatrix(
    rows: Int = 3,
    cols: Int = 3,
    dotSize: Dp = 3.dp,
    dotColor: Color = RemInkBlack,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        repeat(rows) {
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                repeat(cols) {
                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .background(dotColor, CircleShape)
                    )
                }
            }
        }
    }
}

/**
 * 2D Cartoon Neo-brutalist REM Robot Mascot Icon.
 */
@Composable
fun RemMascotGraphic(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .background(RemPrimaryContainer, RoundedCornerShape(10.dp))
            .border(2.dp, RemInkBlack, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.75f)) {
            val w = this.size.width
            val h = this.size.height

            // Antenna
            drawLine(
                color = RemInkBlack,
                start = Offset(w * 0.5f, h * 0.05f),
                end = Offset(w * 0.5f, h * 0.22f),
                strokeWidth = 2.dp.toPx()
            )
            drawCircle(
                color = RemSecondaryContainer,
                radius = 3.dp.toPx(),
                center = Offset(w * 0.5f, h * 0.06f)
            )
            drawCircle(
                color = RemInkBlack,
                radius = 3.dp.toPx(),
                center = Offset(w * 0.5f, h * 0.06f),
                style = Stroke(1.5.dp.toPx())
            )

            // Robot Head
            val headPath = Path().apply {
                addRoundRect(
                    androidx.compose.ui.geometry.RoundRect(
                        left = w * 0.12f,
                        top = h * 0.22f,
                        right = w * 0.88f,
                        bottom = h * 0.90f,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx())
                    )
                )
            }
            drawPath(headPath, color = RemPrimaryContainer)
            drawPath(headPath, color = RemInkBlack, style = Stroke(2.dp.toPx()))

            // Eyes
            drawCircle(
                color = RemInkBlack,
                radius = 3.dp.toPx(),
                center = Offset(w * 0.35f, h * 0.48f)
            )
            drawCircle(
                color = Color.White,
                radius = 1.dp.toPx(),
                center = Offset(w * 0.33f, h * 0.45f)
            )

            drawCircle(
                color = RemInkBlack,
                radius = 3.dp.toPx(),
                center = Offset(w * 0.65f, h * 0.48f)
            )
            drawCircle(
                color = Color.White,
                radius = 1.dp.toPx(),
                center = Offset(w * 0.63f, h * 0.45f)
            )

            // Smile
            val smilePath = Path().apply {
                moveTo(w * 0.42f, h * 0.64f)
                quadraticBezierTo(w * 0.50f, h * 0.74f, w * 0.58f, h * 0.64f)
            }
            drawPath(
                smilePath,
                color = RemInkBlack,
                style = Stroke(width = 2.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )

            // Rosy cheeks
            drawCircle(
                color = RemSecondaryContainer,
                radius = 2.dp.toPx(),
                center = Offset(w * 0.25f, h * 0.60f)
            )
            drawCircle(
                color = RemSecondaryContainer,
                radius = 2.dp.toPx(),
                center = Offset(w * 0.75f, h * 0.60f)
            )
        }
    }
}

/**
 * Chip / Badge with neo-brutalist styling.
 */
@Composable
fun BrutalistBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = RemPrimaryContainer,
    textColor: Color = RemOnSurface,
    borderColor: Color = RemInkBlack,
    borderWidth: Dp = 1.5.dp
) {
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

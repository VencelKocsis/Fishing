package hu.bme.aut.android.fishing.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Trophy: ImageVector
    get() {
        if (_Trophy != null) {
            return _Trophy!!
        }
        _Trophy = ImageVector.Builder(
            name = "Trophy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(6f, 9f)
                horizontalLineTo(4.5f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -5f)
                horizontalLineTo(6f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(18f, 9f)
                horizontalLineToRelative(1.5f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -5f)
                horizontalLineTo(18f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(4f, 22f)
                horizontalLineToRelative(16f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(10f, 14.66f)
                verticalLineTo(17f)
                curveToRelative(0f, 0.55f, -0.47f, 0.98f, -0.97f, 1.21f)
                curveTo(7.85f, 18.75f, 7f, 20.24f, 7f, 22f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(14f, 14.66f)
                verticalLineTo(17f)
                curveToRelative(0f, 0.55f, 0.47f, 0.98f, 0.97f, 1.21f)
                curveTo(16.15f, 18.75f, 17f, 20.24f, 17f, 22f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(18f, 2f)
                horizontalLineTo(6f)
                verticalLineToRelative(7f)
                arcToRelative(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 0f)
                verticalLineTo(2f)
                close()
            }
        }.build()
        return _Trophy!!
    }

private var _Trophy: ImageVector? = null

package naukri.engine

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class RectRender() : Render() {

    constructor(width: Float, height: Float) : this() {
        right = width / 2
        bottom = height / 2
        left = -right
        top = -bottom
    }

    constructor(left: Float, top: Float, right: Float, bottom: Float) : this() {
        this.left = left
        this.right = right
        this.top = top
        this.bottom = bottom
    }

    constructor(width: Float, height: Float, paint: Paint) : this(width, height) {
        color = paint.color
        strokeWidth = paint.strokeWidth
        style = paint.style
    }

    constructor(left: Float, top: Float, right: Float, bottom: Float, paint: Paint)
            : this(left, top, right, bottom) {
        color = paint.color
        strokeWidth = paint.strokeWidth
        style = paint.style
    }

    var left = 0F

    var top = 0F

    var right = 0F

    var bottom = 0F

    var color = Color.WHITE

    var strokeWidth = 2F

    var style = Paint.Style.STROKE

    val rect get() = RectF(left, top, right, bottom)

    // 世界坐標軸下的 rect
    val worldRect
        get() = RectF(
            renderPosition.x + left,
            renderPosition.y + top,
            renderPosition.x + right,
            renderPosition.y + bottom
        )

    val width get() = right - left

    val height get() = bottom - top

    val paint: Paint
        get() {
            val p = Paint()
            p.color = color
            p.strokeWidth = strokeWidth
            p.style = style
            return p
        }

    override fun render() {
        canvas?.drawRect(
            worldRect, paint
        )
    }
}
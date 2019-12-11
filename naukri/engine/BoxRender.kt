package naukri.engine

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class BoxRender() : Render() {

    class Bounds(private val target: BoxRender) {
        val size
            get() = Vector2(
                target.size.x * target.transform.scale.x,
                target.size.y * target.transform.scale.y
            )

        val left get() = target.renderPosition.x - (size.x / 2)

        val top get() = target.renderPosition.y + (size.y / 2)

        val right get() = target.renderPosition.x + (size.x / 2)

        val bottom get() = target.renderPosition.y - (size.y / 2)

        val rect get() = RectF(left, top, right, bottom)
    }

    constructor(width: Float, height: Float) : this() {
        size.x = width
        size.y = height
    }

    constructor(width: Float, height: Float, paint: Paint) : this(width, height) {
        color = paint.color
        strokeWidth = paint.strokeWidth
        style = paint.style
    }

    var size = Vector2(0F, 0F)

    var color = Color.WHITE

    var strokeWidth = 2F

    var style = Paint.Style.STROKE

    val left get() = renderPosition.x - (size.x / 2)

    val top get() = renderPosition.y + (size.y / 2)

    val right get() = renderPosition.x + (size.x / 2)

    val bottom get() = renderPosition.y - (size.y / 2)

    val rect get() = RectF(left, top, right, bottom)

    val bounds get() = Bounds(this)

    // 世界坐標軸下的 rect
    val worldRect
        get() = RectF(
            renderPosition.x + left * transform.scale.x,
            renderPosition.y + top * transform.scale.y,
            renderPosition.x + right * transform.scale.x,
            renderPosition.y + bottom * transform.scale.y
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
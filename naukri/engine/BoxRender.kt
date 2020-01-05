package naukri.engine

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class BoxRender() : Render(defaultPaint) {

    companion object {
        val defaultPaint = Paint().set {
            it.color = Color.WHITE
            it.strokeWidth = 2F
            it.style = Paint.Style.STROKE
        }
    }

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

    var size = Vector2(0F, 0F)

    constructor(width: Float, height: Float) : this() {
        size.x = width
        size.y = height
    }

    constructor(awake: (BoxRender) -> Unit) : this() {
        this.lateConstructor = { awake(this) }
    }

    constructor(width: Float, height: Float, awake: (BoxRender) -> Unit) : this(width, height) {
        this.lateConstructor = { awake(this) }
    }

    val left get() = renderPosition.x - (size.x / 2)

    val top get() = renderPosition.y + (size.y / 2)

    val right get() = renderPosition.x + (size.x / 2)

    val bottom get() = renderPosition.y - (size.y / 2)

    val rect get() = RectF(left, top, right, bottom)

    val bounds get() = Bounds(this)

    // 渲染器坐標軸 (y軸相反)
    private val renderRectF
        get() = RectF(
            bounds.left,
            bounds.bottom,
            bounds.right,
            bounds.top
        )

    override fun render() {
        canvas?.drawRect(
            renderRectF, paint
        )
    }
}
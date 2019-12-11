package naukri.engine

import android.graphics.Color
import android.graphics.Paint

class CircleRender() : Render() {

    constructor(radius: Float) : this() {
        this.radius = radius
    }

    constructor(radius: Float, paint: Paint) : this(radius) {
        color = paint.color
        strokeWidth = paint.strokeWidth
        style = paint.style
    }

    var radius = 0F

    var color = Color.WHITE

    var strokeWidth = 2F

    var style = Paint.Style.STROKE

    val paint: Paint
        get() {
            val p = Paint()
            p.color = color
            p.strokeWidth = strokeWidth
            p.style = style
            return p
        }

    override fun render() {
        canvas?.drawCircle(renderPosition.x, renderPosition.y, radius, paint)
    }
}

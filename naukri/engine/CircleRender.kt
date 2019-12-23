package naukri.engine

import android.graphics.Color
import android.graphics.Paint

class CircleRender() : Render() {

    class Bounds(target: CircleRender) {
        // 縮放率只受 x 軸控制、亦會因x軸改變而同時改變y來保持圓型而非變形成橢圓
        var radius = target.radius * target.transform.scale.x
    }

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

    var alpha = 255

    private val paint: Paint
        get() {
            val p = Paint()
            p.color = color
            p.strokeWidth = strokeWidth
            p.style = style
            p.alpha = alpha
            return p
        }

    val bounds get() = Bounds(this)

    override fun render() {
        canvas?.drawCircle(renderPosition.x, renderPosition.y, radius, paint)
    }
}

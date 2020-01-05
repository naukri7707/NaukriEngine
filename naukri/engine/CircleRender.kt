package naukri.engine

import android.graphics.Color
import android.graphics.Paint

class CircleRender() : Render(defaultPaint) {

    companion object {
        val defaultPaint = Paint().set {
            it.color = Color.WHITE
            it.strokeWidth = 2F
            it.style = Paint.Style.FILL
        }
    }

    class Bounds(target: CircleRender) {
        // 縮放率只受 x 軸控制、亦會因x軸改變而同時改變y來保持圓型而非變形成橢圓
        var radius = target.radius * target.transform.scale.x
    }

    var radius = 0F

    constructor(radius: Float) : this() {
        this.radius = radius
    }

    constructor(awake: (CircleRender) -> Unit) : this() {
        lateConstructor = { awake(this) }
    }

    constructor(radius: Float, awake: (CircleRender) -> Unit) : this(radius) {
        lateConstructor = { awake(this) }
    }

    val bounds get() = Bounds(this)

    override fun render() {
        canvas?.drawCircle(renderPosition.x, renderPosition.y, radius, paint)
    }
}

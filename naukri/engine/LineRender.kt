package naukri.engine

import android.graphics.Color
import android.graphics.Paint

// !! 畫不出來
class LineRender() : Render() {

    constructor(start: Vector2, end: Vector2) : this() {
        this.start = start
        this.end = end
    }

    constructor(startX: Float, startY: Float, endX: Float, endY: Float)
            : this(Vector2(startX, startY), Vector2(endX, endY))

    constructor(start: Vector2, end: Vector2, paint: Paint) : this(start, end) {
        color = paint.color
        strokeWidth = paint.strokeWidth
        style = paint.style
    }

    constructor(startX: Float, startY: Float, endX: Float, endY: Float, paint: Paint)
            : this(Vector2(startX, startY), Vector2(endX, endY), paint)

    var start = Vector2()

    var end = Vector2()

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
        canvas?.drawLine(start.x, start.y, end.x, end.y, paint)
    }
}

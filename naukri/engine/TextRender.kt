package naukri.engine

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class TextRender() : Render() {

    constructor(text: String) : this() {
        this.text = text
    }

    constructor(text: String, paint: Paint) : this(text) {
        color = paint.color
        strokeWidth = paint.strokeWidth
        textSize = paint.textSize
        textAlign = paint.textAlign
    }

    var text: String = ""

    var color = Color.WHITE

    var strokeWidth = 20F

    var textSize = 90F

    var textAlign = Paint.Align.CENTER

    val paint: Paint
        get() {
            val p = Paint()
            p.color = color
            p.strokeWidth = strokeWidth
            p.textSize = textSize
            p.textAlign = textAlign
            return p
        }

    override fun render() {
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        canvas?.drawText(
            text,
            renderPosition.x,
            renderPosition.y + (rect.height() shr 1),
            paint
        )
    }
}
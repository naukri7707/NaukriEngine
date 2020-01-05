package naukri.engine

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class TextRender() : Render(defaultPaint) {

    companion object {
        val defaultPaint = Paint().set {
            it.color = Color.WHITE
            it.strokeWidth = 20F
            it.textSize = 90F
            it.textAlign = Paint.Align.CENTER
        }
    }

    var text: String = ""

    constructor(text: String) : this() {
        this.text = text
    }

    constructor(awake: (TextRender) -> Unit) : this() {
        lateConstructor = { awake(this) }
    }

    constructor(text: String, awake: (TextRender) -> Unit) : this(text) {
        lateConstructor = { awake(this) }
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
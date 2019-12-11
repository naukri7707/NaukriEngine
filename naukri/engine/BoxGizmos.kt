package naukri.engine

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class BoxGizmos(
    var target: BoxCollider
) : Render() {

    override fun render() {
        val p = Paint()
        p.color = Color.GREEN
        p.strokeWidth = 2F
        p.style = Paint.Style.STROKE
        canvas?.drawRect(
            RectF(
                reanderCenter.x - (target.size.x / 2),
                reanderCenter.y - (target.size.y / 2),
                reanderCenter.x + (target.size.x / 2),
                reanderCenter.y + (target.size.y / 2)
            ), p
        )
    }
}
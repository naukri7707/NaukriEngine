package naukri.engine

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

internal class Gizmos(
    var target: Collider
) : Render(paint) {

    companion object {
        private const val color = Color.GREEN
        private const val strokeWidth = 2F
        private val style = Paint.Style.STROKE

        val paint = Paint().set {
            it.color = Color.GREEN
            it.strokeWidth = strokeWidth
            it.style = style
        }
    }

    var drawFunction = {}

    constructor(target: Collider, awake: (Gizmos) -> Unit) : this(target) {
        lateConstructor = { awake(this) }
    }

    override val renderPosition: Vector2
        get() = target.colliderPosition.worldToRenderPosition()

    override fun iAwake() {
        super.iAwake()
        when (target) {
            is BoxCollider -> drawFunction = { drawBoxGizmos() }
            is CircleCollider -> drawFunction = { drawCircleGizmos() }
        }
    }

    override fun render() {
        drawFunction()
    }

    private fun drawBoxGizmos() {
        val target = this.target as BoxCollider
        canvas?.drawRect(
            RectF(
                renderPosition.x - (target.bounds.size.x / 2),
                renderPosition.y - (target.bounds.size.y / 2),
                renderPosition.x + (target.bounds.size.x / 2),
                renderPosition.y + (target.bounds.size.y / 2)
            ), paint
        )
    }

    private fun drawCircleGizmos() {
        val target = this.target as CircleCollider
        canvas?.drawCircle(
            renderPosition.x,
            renderPosition.y,
            target.radius * target.transform.scale.x,
            paint
        )
    }
}
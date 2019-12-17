package naukri.engine

import java.io.Serializable
import kotlin.math.sqrt

data class Vector2(
    var x: Float = 0F,
    var y: Float = 0F
) : Serializable {

    companion object {

        // 線性插值
        fun lerp(from: Vector2, to: Vector2, proportion: Float): Vector2 {
            return from - (from - to) * proportion
        }

        // 距離
        fun distance(from: Vector2, to: Vector2): Float {
            val m = from - to
            return sqrt(m.x * m.x + m.y * m.y)
        }

        // 限定範圍
        fun clamp(value: Vector2, min: Vector2, max: Vector2): Vector2 {
            when {
                value.x < min.x -> value.x = min.x
                value.x > min.x -> value.x = max.x
            }
            when {
                value.y < min.y -> value.y = min.y
                value.y > min.y -> value.y = max.y
            }
            return value
        }
    }

    operator fun plus(other: Vector2): Vector2 {
        return Vector2(this.x + other.x, this.y + other.y)
    }

    operator fun minus(other: Vector2): Vector2 {
        return Vector2(this.x - other.x, this.y - other.y)
    }

    operator fun times(other: Float): Vector2 {
        return Vector2(this.x * other, this.y * other)
    }

    operator fun div(other: Float): Vector2 {
        return Vector2(this.x / other, this.y / other)
    }

    operator fun unaryMinus(): Vector2 {
        return Vector2() - this
    }

    fun toVector2Int(): Vector2Int {
        return Vector2Int(x.toInt(), y.toInt())
    }

    internal fun renderToWorldPosition(): Vector2 {
        return Vector2(
            x - GameView.renderCenter.x + Camera.position.x,
            GameView.renderCenter.y - y + Camera.position.y
        )
    }

    internal fun worldToRenderPosition(): Vector2 {
        return Vector2(
            GameView.renderCenter.x + x - Camera.position.x,
            GameView.renderCenter.y - y + Camera.position.y
        )
    }
}
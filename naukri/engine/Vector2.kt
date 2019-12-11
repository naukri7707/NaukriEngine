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
            return (from + to) * proportion
        }

        // 距離
        fun distance(from: Vector2, to: Vector2): Float {
            val m = from - to
            return sqrt(m.x * m.x + m.y * m.y)
        }

        // 距離 ( 讓 V3 忽略 Z 軸計算)
        fun distance(from: Vector3, to: Vector3): Float {
            val m = from - to
            return sqrt(m.x * m.x + m.y * m.y)
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

    override fun toString(): String {
        return "($x, $y)"
    }

    fun toVector2Int(): Vector2Int {
        return Vector2Int(x.toInt(), y.toInt())
    }

    fun toVector3(): Vector3 {
        return Vector3(x, y, 0F)
    }

    fun toVector3Int(): Vector3Int {
        return Vector3Int(x.toInt(), y.toInt(), 0)
    }
}
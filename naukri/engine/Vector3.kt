package naukri.engine

import java.io.Serializable
import kotlin.math.sqrt


data class Vector3(
    var x: Float = 0F,
    var y: Float = 0F,
    var z: Float = 0F
) : Serializable {

    companion object {
        // 線性插值
        fun Lerp(from: Vector3, to: Vector3, proportion: Float): Vector3 {
            return (from + to) * proportion
        }

        // 距離
        fun distance(from: Vector3, to: Vector3): Float {
            val m = from - to
            return sqrt(m.x * m.x + m.y * m.y + m.z * m.z)
        }
    }

    operator fun plus(other: Vector3): Vector3 {
        return Vector3(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    operator fun minus(other: Vector3): Vector3 {
        return Vector3(this.x - other.x, this.y - other.y, this.z - other.z)
    }

    operator fun times(other: Float): Vector3 {
        return Vector3(this.x * other, this.y * other, this.z * other)
    }

    operator fun div(other: Float): Vector3 {
        return Vector3(this.x / other, this.y / other, this.z / other)
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }

    fun toVector2(): Vector2 {
        return Vector2(x, y)
    }

    fun toVector2Int(): Vector2Int {
        return Vector2Int(x.toInt(), y.toInt())
    }

    fun toVector3Int(): Vector3Int {
        return Vector3Int(x.toInt(), y.toInt(), z.toInt())
    }

}
package naukri.engine

import java.io.Serializable


data class Vector3Int(
    var x: Int = 0,
    var y: Int = 0,
    var z: Int = 0
) : Serializable {

    operator fun plus(other: Vector3Int): Vector3Int {
        return Vector3Int(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    operator fun minus(other: Vector3Int): Vector3Int {
        return Vector3Int(this.x - other.x, this.y - other.y, this.z - other.z)
    }

    operator fun times(other: Int): Vector3Int {
        return Vector3Int(this.x * other, this.y * other, this.z * other)
    }

    operator fun div(other: Int): Vector3Int {
        return Vector3Int(this.x / other, this.y / other, this.z / other)
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }

    fun toVector2(): Vector2 {
        return Vector2(x.toFloat(), y.toFloat())
    }

    fun toVector2Int(): Vector2Int {
        return Vector2Int(x, y)
    }

    fun toVector3(): Vector3 {
        return Vector3(x.toFloat(), y.toFloat(), z.toFloat())
    }
}
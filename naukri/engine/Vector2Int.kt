package naukri.engine

import java.io.Serializable

data class Vector2Int(
    var x: Int = 0,
    var y: Int = 0
) : Serializable {

    operator fun plus(other: Vector2Int): Vector2Int {
        return Vector2Int(this.x + other.x, this.y + other.y)
    }

    operator fun minus(other: Vector2Int): Vector2Int {
        return Vector2Int(this.x - other.x, this.y - other.y)
    }

    operator fun times(other: Int): Vector2Int {
        return Vector2Int(this.x * other, this.y * other)
    }

    operator fun div(other: Int): Vector2Int {
        return Vector2Int(this.x / other, this.y / other)
    }

    override fun toString(): String {
        return "($x, $y)"
    }

    fun toVector2(): Vector2 {
        return Vector2(x.toFloat(), y.toFloat())
    }

    fun toVector3(): Vector3 {
        return Vector3(x.toFloat(), y.toFloat(), 0F)
    }

    fun toVector3Int(): Vector3Int {
        return Vector3Int(x, y, 0)
    }

}
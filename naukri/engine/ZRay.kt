package naukri.engine

// 這是一個不相依於 GameObject 的元件，僅用做 onTouch 判斷
internal class ZRay() : Collider() {

    constructor(x: Int, y: Int) : this() {
        position.x = x.toFloat()
        position.y = y.toFloat()
    }

    constructor(x: Float, y: Float) : this() {
        position.x = x
        position.y = y
    }

    constructor(position: Vector2) : this() {
        this.position = position
    }

    var position
        get() = offset
        set(value) {
            offset = value
        }

    override fun <T : Collider> isCollision(other: T): Boolean {
        when (other) {
            is BoxCollider -> {
                val o = other as BoxCollider
                return (position.x in (o.bounds.left..o.bounds.right) && position.y in (o.bounds.bottom..o.bounds.top))
            }
            is CircleCollider -> {
                val dis = Vector2.distance(position, other.colliderPosition)
                return dis < other.bounds.radius
            }
        }
        return false
    }

    override fun drawGizmos() {
        // don't need to draw
    }
}
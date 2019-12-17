package naukri.engine

import android.graphics.RectF
import kotlin.math.*

class BoxCollider : Collider() {

    class Bounds(private val target: BoxCollider) {
        val size
            get() = Vector2(
                target.size.x * target.transform.scale.x,
                target.size.y * target.transform.scale.y
            )

        val left get() = target.colliderPosition.x - (size.x / 2)

        val top get() = target.colliderPosition.y + (size.y / 2)

        val right get() = target.colliderPosition.x + (size.x / 2)

        val bottom get() = target.colliderPosition.y - (size.y / 2)

        val rect get() = RectF(left, top, right, bottom)
    }

    var size = Vector2(0F, 0F)

    val left get() = colliderPosition.x - (size.x / 2)

    val top get() = colliderPosition.y + (size.y / 2)

    val right get() = colliderPosition.x + (size.x / 2)

    val bottom get() = colliderPosition.y - (size.y / 2)

    val rect get() = RectF(left, top, right, bottom)

    val bounds get() = Bounds(this)

    override fun iAwake() {
        super.iAwake()
        setSizeAsSpriteRender()
    }

    override fun <T : Collider> isCollision(other: T): Boolean {
        when (other) {
            is BoxCollider -> {
                val o = other as BoxCollider
                return ((o.bounds.left in (bounds.left..bounds.right) || o.bounds.right in (bounds.left..bounds.right)) &&
                        (o.bounds.top in (bounds.bottom..bounds.top) || o.bounds.bottom in (bounds.bottom..bounds.top)))
            }
            is CircleCollider -> {
                // 將範圍縮限在第一象限內
                // 圓心和矩形中心的曼哈頓距離
                val dis = Vector2(
                    abs(colliderPosition.x - other.colliderPosition.x),
                    abs(colliderPosition.y - other.colliderPosition.y)
                )
                // 四分之一的矩形，也就是在第一象限的矩形
                val quarterSize = bounds.size / 2F
                // 圓形後的半徑
                val boundsRadius = other.bounds.radius
                // 開始判斷
                return when {
                    // 如果兩中心點距離超過 rect的長/2 + circle的半徑 則必不相交
                    dis.x > quarterSize.x + boundsRadius || dis.y > quarterSize.y + boundsRadius -> false
                    // 如果兩中心點的其中一軸範圍在矩形內，由於上面判斷的關係另一軸必定 < rect的長/2 + circle的半徑，而這種情況下必定相交
                    dis.x <= quarterSize.x || dis.y <= quarterSize.y -> true
                    // 剩下圓和角落碰撞的可能性，此時若圓心到矩形角落的距離小於圓的半徑及為碰撞
                    else -> Vector2.distance(dis, quarterSize) <= boundsRadius
                }
            }
        }
        return false
    }

    fun setSizeAsSpriteRender() {
        val sr = getComponent<SpriteRender>()
        if (sr != null) {
            size = sr.size.toVector2()
        }
    }

    override fun drawGizmos() {
        getComponents<Gizmos>().forEach {
            if (it.target == this) {
                return
            }
        }
        gameObject.addComponent(Gizmos(this))
    }

}
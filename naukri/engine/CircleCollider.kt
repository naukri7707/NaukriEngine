package naukri.engine

import kotlin.math.abs

class CircleCollider : Collider() {

    class Bounds(target: CircleCollider) {
        // 縮放率只受 x 軸控制、亦會因x軸改變而同時改變y來保持圓型而非變形成橢圓
        var radius = target.radius * target.transform.scale.x
    }

    var radius = 0F

    val bounds get() = Bounds(this)

    override fun <T : Collider> isCollision(other: T): Boolean {
        when (other) {
            is BoxCollider -> {
                // 將範圍縮限在第一象限內
                // 圓心和矩形中心的曼哈頓距離
                val dis = Vector2(
                    abs(colliderPosition.x - other.colliderPosition.x),
                    abs(colliderPosition.y - other.colliderPosition.y)
                )
                // 四分之一的矩形，也就是在第一象限的矩形
                val quarterSize = other.bounds.size / 2F
                // 圓形後的半徑
                val boundsRadius = bounds.radius
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
            is CircleCollider -> {
                val dis = Vector2.distance(
                    gameObject.transform.position,
                    other.transform.position
                )
                return dis <= bounds.radius + other.bounds.radius
            }
        }
        return false
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
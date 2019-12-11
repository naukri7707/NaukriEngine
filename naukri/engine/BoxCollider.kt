package naukri.engine

import kotlin.math.abs

class BoxCollider() : Collision() {

    var size = Vector2(0F, 0F)

    var offset = Vector2(0F, 0F)

    val left get() = transform.position.x + offset.x - (size.x / 2) * abs(transform.scale.x)

    val top get() = transform.position.y + offset.y + (size.y / 2) * abs(transform.scale.y)

    val right get() = transform.position.x + offset.x + (size.x / 2) * abs(transform.scale.x)

    val bottom get() = transform.position.y + offset.y - (size.y / 2) * abs(transform.scale.y)

    override fun <T> isCollision(other: T): Boolean where T : Collision {
        when (other) {
            is BoxCollider -> {
                val o = other as BoxCollider
                return ((o.left in (left..right) || o.right in (left..right)) && (o.top in (bottom..top) || o.bottom in (bottom..top)))
            }
            // TODO Circle Collider https://www.zhihu.com/question/24251545
        }
        return false
    }

    fun setSizeAsSpriteRender() {
        val sr = getComponent<SpriteRender>()
        if(sr != null) {
            size = sr.size.toVector2()
        }
    }

    fun drawCollider() {
        if (getComponent<BoxGizmos>() == null) {
            gameObject.addComponent(BoxGizmos(this))
        }
    }

    override fun iAwake() {
        super.iAwake()
        setSizeAsSpriteRender()
    }
}
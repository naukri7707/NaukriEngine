package naukri.engine


abstract class Collider : Component() {

    companion object {

        internal val collection = ArrayList<Collider>(256)

        internal val onCollisionEvents = ArrayList<() -> Unit>(256)

        internal val onTouchEvents = ArrayList<() -> Unit>(32)

        internal val onHoldEvents = ArrayList<Behaviour>(8)

        fun getOnTouchDownEvents() {
            // TO world position
            val touch = ZRay(ScreenEvent.x, ScreenEvent.y)
            collection.sortedWith(compareBy({ it.gameObject.layer }, { it.transform.zIndex }))
                .forEach { col ->
                    if (!col.isTrigger && touch.isCollision(col)) {
                        col.gameObject.getComponents<Behaviour>().filter { it.enable }.forEach {
                            onTouchEvents.add { it.onTouchDown() }
                            onHoldEvents.add(it)
                        }
                        return
                    }
                }
        }

        fun getOnTouchMoveEvents() {
            // TO world position
            val touch = ZRay(ScreenEvent.x, ScreenEvent.y)
            collection.sortedWith(compareBy({ it.gameObject.layer }, { it.transform.zIndex }))
                .forEach { col ->
                    if (!col.isTrigger && touch.isCollision(col)) {
                        col.gameObject.getComponents<Behaviour>().filter { it.enable }.forEach {
                            onTouchEvents.add { it.onTouchMove() }
                        }
                        return
                    }
                }
        }

        fun getOnTouchUpEvents() {
            onHoldEvents.clear()
            // TO world position
            val touch = ZRay(ScreenEvent.x, ScreenEvent.y)
            collection.sortedWith(compareBy({ it.gameObject.layer }, { it.transform.zIndex }))
                .forEach { col ->
                    if (!col.isTrigger && touch.isCollision(col)) {
                        col.gameObject.getComponents<Behaviour>().filter { it.enable }.forEach {
                            onTouchEvents.add { it.onTouchUp() }
                        }
                        return
                    }
                }
        }

        fun getOnCollisionEvents() {
            collection.forEach { collision ->
                collection.forEach { other ->
                    if (!collision.isTrigger && other != collision && collision.isCollision(other)) {
                        collision.gameObject.getComponents<Behaviour>().filter { it.enable }
                            .forEach {
                                onCollisionEvents.add { it.onCollision(other) }
                            }
                    }
                }
            }
        }

        fun drawGizmos(){
            collection.forEach {
                it.drawGizmos()
            }
        }

    }

    // 觸發器只能被動被觸發而沒有主動觸發其他物件的能力，且不具有 onTouch 系列的特性
    var isTrigger = false

    // 偏移量
    var offset = Vector2(0F, 0F)

    // 碰撞器中心點
    val colliderPosition
        get() = Vector2(
            transform.worldPosition.x + offset.x,
            transform.worldPosition.y + offset.y
        )

    abstract fun <T : Collider> isCollision(other: T): Boolean

    internal abstract fun drawGizmos()

    override fun iAwake() {
        super.iAwake()
        collection.add(this)
    }

    final override fun iOnEnable() {
        super.iOnEnable()
        collection.add(this)
    }

    final override fun iOnDisable() {
        collection.remove(this)
        super.iOnDisable()
    }

    final override fun iOnDestroy() {
        collection.remove(this)
        super.iOnDestroy()
    }
}
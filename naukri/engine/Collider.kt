package naukri.engine


abstract class Collider : Component() {

    companion object {

        internal val collection = ArrayList<Collider>(256)

        internal val onCollisionEvents = ArrayList<() -> Unit>(256)

        internal val onTouchEvents = ArrayList<() -> Unit>(32)

        internal val onHoldEvents = ArrayList<Behaviour>(8)

        fun getOnTouchDownEvents() {
            // TO world localPosition
            val touch = ZRay(ScreenEvent.x, ScreenEvent.y)
            collection.sortedWith(compareBy({ it.gameObject.layer }, { it.transform.zIndex }))
                .forEach { col ->
                    if (touch.isCollision(col)) {
                        col.gameObject.getComponents<Behaviour>().filter { it.enable }.forEach {
                            onTouchEvents.add { it.onTouchDown() }
                            onHoldEvents.add(it)
                        }
                        return
                    }
                }
        }

        fun getOnTouchMoveEvents() {
            // TO world localPosition
            val touch = ZRay(ScreenEvent.x, ScreenEvent.y)
            collection.sortedWith(compareBy({ it.gameObject.layer }, { it.transform.zIndex }))
                .forEach { col ->
                    if (touch.isCollision(col)) {
                        col.gameObject.getComponents<Behaviour>().filter { it.enable }.forEach {
                            onTouchEvents.add { it.onTouchMove() }
                        }
                        return
                    }
                }
        }

        fun getOnTouchUpEvents() {
            onHoldEvents.clear()
            // TO world localPosition
            val touch = ZRay(ScreenEvent.x, ScreenEvent.y)
            collection.sortedWith(compareBy({ it.gameObject.layer }, { it.transform.zIndex }))
                .forEach { col ->
                    if (touch.isCollision(col)) {
                        col.gameObject.getComponents<Behaviour>().filter { it.enable }.forEach {
                            onTouchEvents.add { it.onTouchUp() }
                        }
                        return
                    }
                }
        }

        fun getOnCollisionEvents() {
            collection.forEach { collider ->
                collection.forEach { other ->
                    if (other != collider && collider.isCollision(other)) {
                        when (collider.collisions.contains(other)) {
                            // 已經處於碰撞 (Stay)
                            true -> collider.gameObject.getComponents<Behaviour>().filter { it.enable }
                                .forEach {
                                    onCollisionEvents.add { it.onCollisionStay(other) }
                                }
                            // 原本沒有碰撞 (Enter)
                            false -> collider.gameObject.getComponents<Behaviour>().filter { it.enable }
                                .forEach {
                                    onCollisionEvents.add { it.onCollisionEnter(other) }
                                    collider.collisions.add(other)
                                }
                        }
                        // 原本處於碰撞但來離開 (Leave)
                    } else if (collider.collisions.contains(other)) {
                        collider.gameObject.getComponents<Behaviour>().filter { it.enable }
                            .forEach {
                                onCollisionEvents.add { it.onCollisionLeave(other) }
                                collider.collisions.remove(other)
                            }
                    }
                }
            }
        }

        fun drawGizmos() {
            collection.forEach {
                it.drawGizmos()
            }
        }

    }

    // 觸發器只能被動被觸發而沒有主動觸發其他物件的能力，且不具有 onTouch 系列的特性
    // var isTrigger = false

    // 偏移量
    var offset = Vector2(0F, 0F)

    // 碰撞器中心點
    val colliderPosition
        get() = Vector2(
            transform.position.x + offset.x,
            transform.position.y + offset.y
        )

    private val collisions = ArrayList<Collider>(4)

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
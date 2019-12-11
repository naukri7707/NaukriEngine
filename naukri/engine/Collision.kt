package naukri.engine


abstract class Collision : Component() {

    companion object {

        internal val collection = ArrayList<Collision>(256)

        internal val onCollisionEvents = ArrayList<Behaviour>(256)

        internal val onTouchEvents = ArrayList<() -> Unit>(32)

        internal val onHoldEvents = ArrayList<Behaviour>(8)

        fun onTouchDown() {
            val touch = ZRay(ScreenEvent.x, ScreenEvent.y)
            collection.sortedWith(compareBy({ it.gameObject.layer }, { -it.transform.position.z }))
                .forEach { col ->
                    if (touch.isCollision(col) && !col.isTrigger) {
                        col.gameObject.getComponents<Behaviour>().filter { it.enable }.forEach {
                            onTouchEvents.add { it.onTouchDown() }
                            onHoldEvents.add(it)
                        }
                        return
                    }
                }
        }

        fun onTouchMove() {
            val touch = ZRay(ScreenEvent.x, ScreenEvent.y)
            collection.sortedWith(compareBy({ it.gameObject.layer }, { -it.transform.position.z }))
                .forEach { col ->
                    if (touch.isCollision(col) && !col.isTrigger) {
                        col.gameObject.getComponents<Behaviour>().filter { it.enable }.forEach {
                            onTouchEvents.add { it.onTouchMove() }
                        }
                        return
                    }
                }
        }

        fun onTouchUp() {
            val touch = ZRay(ScreenEvent.x, ScreenEvent.y)
            collection.sortedWith(compareBy({ it.gameObject.layer }, { -it.transform.position.z }))
                .forEach { col ->
                    if (touch.isCollision(col) && !col.isTrigger) {
                        col.gameObject.getComponents<Behaviour>().filter { it.enable }.forEach {
                            onTouchEvents.add { it.onTouchUp() }
                        }
                        onHoldEvents.clear()
                        return
                    }
                }
        }

        fun onCollision() {
            collection.forEach { collision ->
                collection.forEach { other ->
                    if (other != collision && collision.isCollision(other)) {
                        collision.gameObject.getComponents<Behaviour>().filter { it.enable }
                            .forEach {
                                onCollisionEvents.add(it)
                            }
                    }
                }
            }
        }

    }

    var isTrigger = false

    abstract fun <T> isCollision(other: T): Boolean where T : Collision

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
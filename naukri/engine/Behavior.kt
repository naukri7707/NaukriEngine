package naukri.engine

abstract class Behaviour : Component() {

    companion object {
        // 活性元件搜集器
        internal val activeCollection = ArrayList<Component>(4096)
    }

    // Component
    protected open fun onEnable() {}

    protected open fun onDisable() {}

    protected open fun onDestroy() {}

    // Behavior
    protected open fun start() {}

    protected open fun earlyUpdate() {}

    protected open fun update() {}

    protected open fun lateUpdate() {}

    // Collider TODO
    open fun onTouchDown() {}

    open fun onTouchHold() {}

    open fun onTouchMove() {}

    open fun onTouchUp() {}

    open fun onCollision(other: Collision) {}


    // iComponent
    final override fun iOnEnable() {
        super.iOnEnable()
        activeCollection.add(this)
        onEnable()
    }

    final override fun iStart() {
        super.iStart()
        activeCollection.add(this)
        start()
    }

    final override fun iEarlyUpdate() {
        super.iEarlyUpdate()
        earlyUpdate()
    }

    final override fun iUpdate() {
        super.iUpdate()
        update()
    }

    final override fun iLateUpdate() {
        super.iLateUpdate()
        lateUpdate()
    }

    final override fun iOnDisable() {
        onDisable()
        activeCollection.remove(this)
        super.iOnDisable()
    }

    final override fun iOnDestroy() {
        onDestroy()
        activeCollection.remove(this)
        Collision.onHoldEvents.remove(this)
        Collision.onCollisionEvents.remove(this)
        super.iOnDestroy()
    }
}
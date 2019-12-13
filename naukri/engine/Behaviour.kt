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

    // Behaviour
    protected open fun awake() {}

    protected open fun start() {}

    protected open fun earlyUpdate() {}

    protected open fun update() {}

    protected open fun lateUpdate() {}

    // Touch Events
    open fun onTouchDown() {}

    open fun onTouchHold() {}

    open fun onTouchMove() {}

    open fun onTouchUp() {}

    // Collision Events
    open fun onCollisionEnter(other: Collider) {}

    open fun onCollisionStay(other: Collider) {}

    open fun onCollisionLeave(other: Collider) {}

    // 關閉接口，但引用給使用者用的乾淨接口 (如果開放)

    final override fun iAwake() {
        super.iAwake()
        awake()
    }

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
    final override fun iOnCollision() {
        super.iOnCollision()
    }

    final override fun iRender() {
        super.iRender()
    }

    final override fun iOnDisable() {
        onDisable()
        activeCollection.remove(this)
        super.iOnDisable()
    }

    final override fun iOnDestroy() {
        onDestroy()
        activeCollection.remove(this)
        Collider.onHoldEvents.remove(this)
        super.iOnDestroy()
    }
}
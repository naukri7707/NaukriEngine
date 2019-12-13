package naukri.engine

abstract class Component : Object() {

    companion object{
        // 啟發式狀態搜集器
        internal val iOnEnableCollection = ArrayList<() -> Unit>(256)
        internal val iStartCollection = ArrayList<() -> Unit>(512)
        internal val iOnDisableCollection = ArrayList<() -> Unit>(256)
        internal val iOnDestroyCollection = ArrayList<() -> Unit>(256)
    }

    lateinit var gameObject: GameObject

    val transform get() = gameObject.transform

    val name get() = gameObject.name

    val tag get() = gameObject.tag

    // 是否實例化 (防止 gameObject 為 null)
    internal var isInstantiate = false
        internal set(value) {
            if (value != field) {
                if (value) {
                    iAwake()
                    if(enable) {
                        iStartCollection.add { iStart() }
                    }
                } else {
                    iOnDestroyCollection.add { iOnDestroy() }
                }
                field = value
            }
        }

    // 啟用
    var enable = true
        set(value) {
            // 嘗試改變狀態
            if (value != field && isInstantiate) {
                if (value && gameObject.enable) {
                    iOnEnableCollection.add { iOnEnable() }
                    iStartCollection.add { iStart() }
                } else if (!value) {
                    iOnDisableCollection.add { iOnDisable() }
                }
                field = value
            }
        }

    // 實例化後
    internal open fun iAwake() {}

    // 啟動時
    internal open fun iOnEnable() {}

    // 啟動後首幀
    internal open fun iStart() {}

    // 每幀碰撞
    internal open fun iOnCollision() {}

    // 每幀早更新
    internal open fun iEarlyUpdate() {}

    // 每幀更新
    internal open fun iUpdate() {}

    // 每幀晚更新
    internal open fun iLateUpdate() {}

    // 每幀渲染
    internal open fun iRender() {}

    // 關閉時
    internal open fun iOnDisable() {}

    // 刪除時
    internal open fun iOnDestroy() {}

    // Create
    fun <T : Component> addComponent(component: T): T {
        component.gameObject = gameObject
        if (isInstantiate) {
            component.isInstantiate = true
        }
        return component
    }

    // Read, Update
    inline fun <reified T : Component> getComponent(): T? {
        gameObject.components.forEach {
            if (it is T) {
                return it
            }
        }
        return null
    }

    inline fun <reified T : Component> getComponents(): Array<T> {
        val res = mutableListOf<T>()
        gameObject.components.forEach {
            if (it is T) {
                res.add(it)
            }
        }
        return res.toTypedArray()
    }
}
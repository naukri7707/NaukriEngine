package naukri.engine

abstract class Component : Object() {

    lateinit var gameObject: GameObject

    val transform get() = gameObject.transform

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
package naukri.engine


import android.content.res.Resources

abstract class Object : java.io.Serializable {

    companion object {
        // 啟發式狀態搜集器
        internal val iOnEnableCollection = ArrayList<() -> Unit>(256)
        internal val iStartCollection = ArrayList<() -> Unit>(512)
        internal val iOnDisableCollection = ArrayList<() -> Unit>(256)
        internal val iOnDestroyCollection = ArrayList<() -> Unit>(256)
        // Resource from MainActivity (Entry)
        lateinit var resources: Resources

        fun instantiate(gameObject: GameObject): GameObject {
            val newObject = gameObject.deepCopy()!!
            newObject.isInstantiate = true
            return newObject
        }

        fun instantiate(vararg gameObjects: GameObject): Array<GameObject> {
            val res = mutableListOf<GameObject>()
            gameObjects.forEach {
                res.add(instantiate(it))
            }
            return res.toTypedArray()
        }

        fun instantiateNonCopy(gameObject: GameObject) : GameObject{
            gameObject.isInstantiate = true
            return gameObject
        }
        // 實例化物件, 如果是實例化內嵌在 instantiate() 裡面的 GameObject 應該使用這個減少運算開銷
        fun instantiateNonCopy(vararg gameObjects : GameObject): Array<GameObject> {
            val res = mutableListOf<GameObject>()
            gameObjects.forEach {
                res.add(instantiateNonCopy(it))
            }
            return res.toTypedArray()
        }

        fun destroy(gameObject: GameObject) {
            gameObject.isInstantiate = false // 觸發 OnDestroy -> components OnDestroy
        }

        fun <T : Component> destroy(target: T) {
            target.isInstantiate = false
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

}
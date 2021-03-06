package naukri.engine

import android.content.res.Resources
import java.io.*

abstract class Object : Serializable {

    companion object {
        private const val serialVersionUID = 27140617107590L

        // Resource from MainActivity (Entry)
        lateinit var resources: Resources

        fun instantiate(gameObject: GameObject): GameObject {
            return gameObject.newInstance()
        }

        fun instantiate(gameObject: GameObject, position: Vector2): GameObject {
            val newObject = gameObject.newInstance()
            newObject.transform.position = position
            return newObject
        }

        fun instantiate(gameObject: GameObject, parent: Transform): GameObject {
            val newObject = gameObject.newInstance()
            newObject.transform.parent = parent
            return newObject
        }

        fun instantiate(vararg gameObjects: GameObject): Array<GameObject> {
            val res = mutableListOf<GameObject>()
            gameObjects.forEach {
                res.add(instantiate(it))
            }
            return res.toTypedArray()
        }

        fun instantiate(gameObject: GameObject, count: Int): Array<GameObject> {
            val res = mutableListOf<GameObject>()
            for (i in 0 until count) {
                res.add(instantiate(gameObject))
            }
            return res.toTypedArray()
        }

        fun instantiateNonCopy(gameObject: GameObject): GameObject {
            gameObject.isInstantiate = true
            return gameObject
        }

        // 實例化物件, 如果是實例化內嵌在 instantiate() 裡面的 GameObject 應該使用這個減少運算開銷
        fun instantiateNonCopy(vararg gameObjects: GameObject): Array<GameObject> {
            val res = mutableListOf<GameObject>()
            gameObjects.forEach {
                res.add(instantiateNonCopy(it))
            }
            return res.toTypedArray()
        }

        fun destroy(gameObject: GameObject) {
            gameObject.isInstantiate = false // 觸發 OnDestroy -> components OnDestroy
            gameObject.children.forEach {
                it.gameObject.isInstantiate = false
            }
        }

        fun <T : Component> destroy(target: T) {
            target.isInstantiate = false
        }
    }
}
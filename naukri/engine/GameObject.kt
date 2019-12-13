package naukri.engine

class GameObject(vararg components: Component) : Object() {

    companion object {
        // 實例化物件搜集器
        private var mInstantiateCollection = ArrayList<GameObject>(1024)

        // 根物件，一切物件的祖物件，新增物件的預設父物件
        private val root = GameObject()

        // 用名子尋找
        fun find(name: String): GameObject? {
            mInstantiateCollection.forEach {
                if (it.name == name) {
                    return it
                }
            }
            return null
        }

        // 用標籤尋找
        fun findObjectsWithTag(tag: String): Array<GameObject> {
            val res = mutableListOf<GameObject>()
            mInstantiateCollection.forEach {
                if (it.tag == tag) {
                    res.add(it)
                }
            }
            return res.toTypedArray()
        }

        // 實例化物件, 如果是實例化預製物件的話應該使用這個
        fun instantiate(gameObject: GameObject): GameObject {
            return Object.instantiate(gameObject)
        }

        fun instantiate(gameObject: GameObject, position: Vector2): GameObject {
            return Object.instantiate(gameObject, position)
        }

        fun instantiate(vararg gameObjects : GameObject): Array<GameObject> {
            return Object.instantiate(*gameObjects)
        }

        fun instantiateNonCopy(gameObject: GameObject) : GameObject{
            return Object.instantiateNonCopy(gameObject)
        }
        // 實例化物件, 如果是實例化內嵌在 instantiate() 裡面的 GameObject 應該使用這個減少運算開銷
        fun instantiateNonCopy(vararg gameObjects : GameObject): Array<GameObject> {
            return Object.instantiateNonCopy(*gameObjects)
        }

        fun destroy(gameObject: GameObject) {
            gameObject.isInstantiate = false // 觸發 OnDestroy -> components OnDestroy
        }

        fun <T : Component> destroy(target: T) {
            target.isInstantiate = false
        }
    }

    // 物件本身
    val gameObject get() = this

    // 轉換器 (元件)
    val transform = Transform()

    // 元件
    val components = ArrayList<Component>()

    // 父物件
    var parent = root
        set(value) {
            field.children.remove(this)
            value.children.add(this)
            field = value
        }

    // 子物件
    val children = ArrayList<GameObject>()

    var name = ""

    var tag = ""

    var layer = Layer.Default

    // 是否實例化
    internal var isInstantiate = false
        set(value) {
            if (value != field) {
                if (value) {
                    mInstantiateCollection.add(this)
                    components.forEach {
                        it.isInstantiate = true
                    }
                } else {
                    components.forEach {
                        it.isInstantiate = false
                    }
                    mInstantiateCollection.remove(this)
                }
                field = value
            }
        }

    // 是否啟用
    var enable = true
        set(value) {
            if (value != field && isInstantiate) {
                if (value) {
                    components.forEach {
                        if (it.enable) {
                            it.enable = true
                        }
                    }
                } else {
                    components.forEach {
                        if (it.enable) {
                            it.enable = false
                        }
                    }
                }
            }
            field = value
        }

    init {
        transform.gameObject = this
        this.components.add(transform)
        for (c in components) {
            c.gameObject = this
            this.components.add(c)
        }
    }

    // 帶有擴充建構函式的建構子，執行順序如下
    // init -> components Awake -> constructFunc
    constructor(
        vararg components: Component,
        constructFunc: (GameObject) -> Unit
    ) : this(*components) {
        constructFunc(this)
    }

    // Create
    fun <T : Component> addComponent(component: T): T {
        component.gameObject = this
        components.add(component)
        if (isInstantiate) {
            component.isInstantiate = true
        }
        return component
    }

    // Read , Update
    inline fun <reified T : Component> getComponent(): T? {
        components.forEach {
            if (it is T) {
                return it
            }
        }
        return null
    }

    inline fun <reified T : Component> getComponents(): Array<T> {
        val res = mutableListOf<T>()
        components.forEach {
            if (it is T) {
                res.add(it)
            }
        }
        return res.toTypedArray()
    }
}
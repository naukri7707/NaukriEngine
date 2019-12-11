package naukri.engine

class GameObject(vararg components: Component) : Object() {

    companion object {

        // 實例化物件搜集器
        private var mInstantiateCollection = ArrayList<GameObject>(1024)

        // 活性元件搜集器
        private val mActiveCollection = ArrayList<GameObject>(512)

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

        fun instantiate(gameObject: GameObject): GameObject {
            val newObject = gameObject.deepCopy()!!
            newObject.isInstantiate = true // 觸發 Awake -> components Awake
            return newObject
        }

        fun instantiate(vararg gameObjects: GameObject): Array<GameObject> {
            val res = mutableListOf<GameObject>()
            gameObjects.forEach {
                res.add(instantiate(it))
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

    val gameObject get() = this

    val transform = Transform()

    val components = ArrayList<Component>()

    // 是否實例化
    internal var isInstantiate = false
        set(value) {
            if (value != field) {
                if (value) {
                    mInstantiateCollection.add(this)
                    iAwake()
                    if(enable) {
                        iStartCollection.add { iStart() }
                    }
                } else {
                    iOnDestroyCollection.add { iOnDestroy() }
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
                    mActiveCollection.add(this)
                    iOnEnableCollection.add { iOnEnable() }
                    iStartCollection.add { iStart() }
                } else {
                    iOnDisableCollection.add { iOnDisable() }
                    mActiveCollection.remove(this)
                }
            }
            field = value
        }

    var name = ""

    var tag = ""

    var layer = Layer.Default

    init {
        transform.gameObject = this
        this.components.add(transform)
        for (c in components) {
            c.gameObject = this
            this.components.add(c)
        }
    }

    constructor(name: String, vararg components: Component) : this(*components) {
        this.name = name
    }

    constructor(name: String, tag: String, vararg components: Component) : this(name, *components) {
        this.tag = tag
    }

    override fun iAwake() {
        components.forEach {
            it.isInstantiate = true
        }
    }

    override fun iOnEnable() {
        components.forEach {
            if (it.enable) {
                it.enable = true
            }
        }
    }

    override fun iOnDisable() {
        components.forEach {
            if (it.enable) {
                it.enable = false
            }
        }
    }

    override fun iOnDestroy() {
        components.forEach {
            if (it.enable) {
                it.isInstantiate = false
            }
        }
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
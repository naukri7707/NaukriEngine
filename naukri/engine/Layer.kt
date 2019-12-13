package naukri.engine

import java.io.Serializable

enum class Layer(val value: Int) : Serializable {
    Background(1),
    World(2),
    Default(268435456),
    UI(536870912),
    Debug(1073741824)
}
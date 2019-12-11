package naukri.engine

import java.io.Serializable

enum class Layer(val value: Int) : Serializable {
    Default(1),
    World(2),
    UI(536870912),
    Debug(1073741824)
}
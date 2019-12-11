package naukri.engine

class Transform : Component() {
    // 相對於父物件的坐標軸
    var position = Vector2(0F, 0F)

    // 倍率 ， 不可為負!
    var scale = Vector2(1F, 1F)
        set(value) {
            if (value.x < 0) value.x = -value.x
            if (value.y < 0) value.y = -value.y
            field = value
        }
    // 圖層索引，越大越上層
    var zIndex = 0F

    // 真實坐標軸
    val worldPosition get() = gameObject.parent.transform.position + position

    fun translate(x: Float, y: Float) {
        position += Vector2(x, y)
    }

    fun translate(translate: Vector2) {
        position += translate
    }
}
package naukri.engine

class Transform : Component() {
    // 相對於父物件的坐標軸
    var position = Vector2(0F, 0F)

    var scale = Vector2(1F, 1F)

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
package naukri.engine

class Transform : Component() {
    // 相對於父物件的坐標軸
    var localPosition = Vector2(0F, 0F)

    // 倍率 ， 不可為負!
    var scale = Vector2(1F, 1F)
        set(value) {
            if (value.x < 0) value.x = -value.x
            if (value.y < 0) value.y = -value.y
            field = value
        }
    // 圖層索引，越大越上層
    var zIndex = 0

    // 真實坐標軸
    var position: Vector2
        get() {
            var res = localPosition
            var current = gameObject
            while (current != current.parent) {
                res += current.parent.transform.localPosition
                current = current.parent
            }
            return res
        }
        set(value) {
            localPosition += value - position
        }

    fun translate(x: Float, y: Float) {
        localPosition += Vector2(x, y)
    }

    fun translate(translate: Vector2) {
        localPosition += translate
    }
}
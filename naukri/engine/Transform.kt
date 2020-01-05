package naukri.engine

import kotlin.concurrent.timer

class Transform() : Component() {
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

    // 父物件
    var parent = this
        set(value) {
            field.children.remove(this)
            value.children.add(this)
            field = value
        }

    // 子物件
    val children = ArrayList<Transform>()

    constructor(awake: (Transform) -> Unit) : this() {
        lateConstructor = { awake(this) }
    }

    // 真實坐標軸
    var position: Vector2
        get() {
            var res = localPosition
            var current = this
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

    /**
     * 限制增量
     * @param x       x軸增量
     * @param y       y軸增量
     * @param limiter 限制器
     */
    fun translate(x: Float, y: Float, limiter: Float) {
        localPosition.x += x.coerceIn(-limiter, limiter)
        localPosition.y += y.coerceIn(-limiter, limiter)
    }

    /**
     * 限制增量
     * @param translate 目標增量
     * @param limiter   限制器
     */
    fun translate(translate: Vector2, limiter: Float) {
        localPosition.x += translate.x.coerceIn(-limiter, limiter)
        localPosition.y += translate.y.coerceIn(-limiter, limiter)
    }

    /**
     * 移動到目標
     * @param target 目標座標
     * @param speed  速度
     */
    fun moveTo(target: Vector2, speed: Float) {
        val c = Vector2.distance(position, target)
        if (c == 0F) {
            return
        }
        val a = target.x - position.x
        val b = target.y - position.y
        val newPos = Vector2(
            position.x + speed * a / c,
            position.y + speed * b / c
        )
        if (Float.distance(position.x, target.x) < Float.distance(position.x, newPos.x)) {
            newPos.x = target.x
        }
        if (Float.distance(position.y, target.y) < Float.distance(position.y, newPos.y)) {
            newPos.y = target.y
        }
        position = newPos
    }
}
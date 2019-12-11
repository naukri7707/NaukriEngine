package naukri.engine

import android.graphics.Canvas

abstract class Render : Component() {

    companion object {

        internal var canvas: Canvas? = null

        internal val collection = ArrayList<Render>(256)
    }

    // 渲染器中心點
    val reanderCenter
        get() = Vector2(
            GameView.viewCenter.x + transform.position.x - Camera.position.x,
            GameView.viewCenter.y - transform.position.y + +Camera.position.y
        )

    var flipX = false

    var flipY = false

    final override fun iAwake() {
        super.iAwake()
        collection.add(this)
    }

    final override fun iOnEnable() {
        super.iOnEnable()
        collection.add(this)
    }

    final override fun iOnDisable() {
        collection.remove(this)
        super.iOnDisable()
    }

    final override fun iOnDestroy() {
        collection.remove(this)
        super.iOnDestroy()
    }

    abstract fun render()
}
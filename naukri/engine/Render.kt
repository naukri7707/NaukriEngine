package naukri.engine

import android.graphics.Canvas

abstract class Render : Component() {

    companion object {

        internal var canvas: Canvas? = null

        internal val collection = ArrayList<Render>(256)
    }

    // 渲染器中心點
    open val renderPosition
        get() = Vector2(
            GameView.renderCenter.x + transform.position.x - Camera.position.x,
            GameView.renderCenter.y - transform.position.y + Camera.position.y
        )

    var flipX = false

    var flipY = false

    override fun iAwake() {
        super.iAwake()
        collection.add(this)
    }

    override fun iOnEnable() {
        super.iOnEnable()
        collection.add(this)
    }

    override fun iOnDisable() {
        collection.remove(this)
        super.iOnDisable()
    }

    override fun iOnDestroy() {
        collection.remove(this)
        super.iOnDestroy()
    }

    abstract fun render()
}
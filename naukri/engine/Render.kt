package naukri.engine

import android.graphics.Canvas
import android.graphics.Paint

abstract class Render(
    paint: Paint
) : Component() {

    companion object {

        var reSort = false

        val defaultPaint = Paint()

        internal var canvas: Canvas? = null

        internal val collection = ArrayList<Render>(256)
    }

    var flipX = false

    var flipY = false

    // 畫筆
    @Transient
    private var mPaint = Paint(paint)

    constructor(paintFunc: (Render, Paint) -> Unit) : this(defaultPaint) {
        paintFunc(this, paint)
    }

    val paint get() = mPaint

    var offset = Vector2()

    // 渲染器中心點
    open val renderPosition
        get() = Vector2(
            GameView.renderCenter.x + transform.position.x + offset.x * transform.scale.x - Camera.position.x,
            GameView.renderCenter.y - transform.position.y - offset.y * transform.scale.y + Camera.position.y
        )

    override fun iAwake() {
        super.iAwake()
        collection.add(this)
        reSort = true
    }

    override fun iOnEnable() {
        super.iOnEnable()
        collection.add(this)
        reSort = false
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

    override fun <T : Component> deepCopyFrom(TValue: T) {
        mPaint = Paint((TValue as Render).mPaint)
    }
}
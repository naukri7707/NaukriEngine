package naukri.engine

import android.graphics.*
import android.media.Image
import kotlin.math.abs


class SpriteRender() : Render() {

    companion object {
        // 讓 BitmapFactory 不要重新採樣
        val options: BitmapFactory.Options by lazy {
            val op = BitmapFactory.Options()
            op.inScaled = false
            return@lazy op
        }
    }


    constructor(sprite: Int) : this() {
        this.sprite = sprite
    }

    class Bounds(private val target: SpriteRender) {
        val size
            get() = Vector2(
                target.size.x * target.transform.scale.x,
                target.size.y * target.transform.scale.y
            )

        val left get() = target.renderPosition.x - (size.x / 2)

        val top get() = target.renderPosition.y + (size.y / 2)

        val right get() = target.renderPosition.x + (size.x / 2)

        val bottom get() = target.renderPosition.y - (size.y / 2)

        val rect get() = RectF(left, top, right, bottom)
    }

    // 大小
    var size = Vector2Int(0, 0)

    val left get() = renderPosition.x - (size.x shr 1)

    val top get() = renderPosition.y + (size.y shr 1)

    val right get() = renderPosition.x + (size.x shr 1)

    val bottom get() = renderPosition.y - (size.y shr 1)

    val rect get() = RectF(left, top, right, bottom)

    val bounds get() = Bounds(this)

    // 精靈 (圖片 ID)
    var sprite = 0
        set(value) {
            field = value
            image = BitmapFactory.decodeResource(resources, sprite, options)
            size = Vector2Int(image.width, image.height)
        }

    // 圖片
    @Transient
    lateinit var image: Bitmap

    // 渲染器坐標軸 (y軸相反)
    private val renderRectF
        get() = RectF(
            bounds.left,
            bounds.bottom,
            bounds.right,
            bounds.top
        )

    override fun iAwake() {
        super.iAwake()
        sprite = sprite // 讓實例化 prefab 時能正確賦予image值
    }

    override fun render() {
        canvas?.drawBitmap(
            image.flip(flipX, flipY), Rect(0, 0, size.x, size.y), renderRectF, null
        )
    }
}
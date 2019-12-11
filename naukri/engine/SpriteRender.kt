package naukri.engine

import android.graphics.*
import kotlin.math.abs


class SpriteRender() : Render() {

    constructor(sprite: Int) : this() {
        this.sprite = sprite
    }

    // 大小
    var size = Vector2Int(0, 0)

    // 精靈 (圖片 ID)
    var sprite = 0
        set(value) {
            field = value
            val img = image
            size = Vector2Int(img.width, img.height)
        }

    // 圖片
    val image get() = BitmapFactory.decodeResource(resources, sprite)

    // 渲染器坐標軸
    val renderRectF
        get() = RectF(
            reanderCenter.x - (size.x shr 1) * abs(transform.scale.x),
            reanderCenter.y - (size.y shr 1) * abs(transform.scale.y),
            reanderCenter.x + (size.x shr 1) * abs(transform.scale.x),
            reanderCenter.y + (size.y shr 1) * abs(transform.scale.y)
        )

    // 世界坐標軸
    val worldRectF
        get() = RectF(
            transform.position.x - (size.x shr 1) * abs(transform.scale.x),
            transform.position.y - (size.y shr 1) * abs(transform.scale.y),
            transform.position.x + (size.x shr 1) * abs(transform.scale.x),
            transform.position.y + (size.y shr 1) * abs(transform.scale.y)
        )

    override fun render() {
        if (image != null) {
            canvas?.drawBitmap(
                image.flip(flipX, flipY), Rect(0, 0, size.x, size.y), renderRectF, null
            )
        }
    }
}
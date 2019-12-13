package naukri.engine

import android.graphics.*
import android.graphics.BitmapFactory


// animator 目前不能翻轉
class Animation() : Render() {

    companion object {
        // 每張精靈預設停留幀數
        var DefaultFrameScale = 3
    }

    class Bounds(private val target: Animation) {
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

    /*
    * spriteSheet     : 精靈表 ID
    * count           : 精靈數量
    * size            : 精靈大小
    * spriteForFrames : 每幀位置
     */
    constructor(
        spriteSheet: Int,
        count: Int,
        size: Vector2Int,
        vararg frames: Int
    ) : this() {
        // 初始設定
        this.sprite = spriteSheet
        this.size = size
        // 設定 spritesPosition
        var idx = 0
        spritesPosition = Array(count) { Vector2Int() }
        loop@ for (height in (size.y..sheetSize.y) step size.y) {
            for (width in (size.x..sheetSize.x) step size.x) {
                if (idx == count) {
                    break@loop
                }
                spritesPosition[idx++] = Vector2Int(width - size.x, height - size.y)
            }
        }
        // 設定 spriteForFrames 保證至少有一張
        this.spriteForFrames = if (frames.isEmpty()) {
            IntArray(count) { it }
        } else {
            frames
        }
    }

    // 大小
    var size = Vector2Int(0, 0)

    private var sheetSize = Vector2Int(0, 0)

    val left get() = renderPosition.x - (size.x shr 1)

    val top get() = renderPosition.y + (size.y shr 1)

    val right get() = renderPosition.x + (size.x shr 1)

    val bottom get() = renderPosition.y - (size.y shr 1)

    val rect get() = RectF(left, top, right, bottom)

    val bounds get() = Bounds(this)

    // 精靈表 ID
    private var sprite = 0
        set(value) {
            field = value
            image = BitmapFactory.decodeResource(resources, sprite, SpriteRender.options)
            sheetSize = Vector2Int(image.width, image.height)
        }

    // 精靈表
    @Transient
    lateinit var image: Bitmap

    // 每幀對應的精靈
    private lateinit var spriteForFrames: IntArray

    @Transient
    var frame = 0

    // 幀數倍率
    var frameScale = DefaultFrameScale

    // 縮放後的幀數
    private val scaleFrame get() = frame / frameScale

    // 各精靈座標軸 (左上角)
    private lateinit var spritesPosition: Array<Vector2Int>

    private var onEnd: (GameObject) -> Unit = {}

    // 渲染器坐標軸 (y軸相反)
    private val renderRectF
        get() = RectF(
            bounds.left,
            bounds.bottom,
            bounds.right,
            bounds.top
        )

    private val currentSpriteRect
        get() = Rect(
            spritesPosition[scaleFrame].x,
            spritesPosition[scaleFrame].y,
            spritesPosition[scaleFrame].x + size.x,
            spritesPosition[scaleFrame].y + size.y
        )


    private fun nextFrame() {
        frame++
        if (scaleFrame >= spritesPosition.size) {
            onEnd(gameObject)
            frame = 0
        }
    }

    public fun setOnEndListener(func: (GameObject) -> Unit) {
        onEnd = func
    }

    override fun iAwake() {
        super.iAwake()
        sprite = sprite
    }

    override fun render() {
        canvas?.drawBitmap(
            image, currentSpriteRect, renderRectF, null
        )
        nextFrame()
    }
}
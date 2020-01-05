package naukri.engine

import android.graphics.*
import android.graphics.BitmapFactory

// TODO animator 完整翻轉
class Animation() : SpriteRender() {

    companion object {
        // 每張精靈預設停留幀數
        var DefaultFrameScale = 3
    }

    constructor(awake: (Animation) -> Unit) : this() {
        this.lateConstructor = { awake(this) }
    }

    /*
    * spriteSheet     : 精靈表 ID
    * touchCount      : 精靈數量
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

    constructor(
        spriteSheet: Int,
        count: Int,
        size: Vector2Int,
        vararg frames: Int,
        lateConstructor: (Animation) -> Unit
    ) : this(spriteSheet, count, size, *frames) {
        this.lateConstructor = { lateConstructor(this) }
    }

    private var sheetSize = Vector2Int()

    // 精靈表 ID
    override var sprite = 0
        set(value) {
            field = value
            image = BitmapFactory.decodeResource(resources, sprite, SpriteRender.options)
            sheetSize = Vector2Int(image.width, image.height)
        }

    // 每幀對應的精靈
    private lateinit var spriteForFrames: IntArray

    @Transient
    var frame = 0

    // 幀數倍率
    var frameScale = DefaultFrameScale


    // 各精靈座標軸 (左上角)
    private lateinit var spritesPosition: Array<Vector2Int>

    private var onEnd: (GameObject) -> Unit = {}

    var playing = true

    // 縮放後的幀數
    private val scaleFrame get() = frame / frameScale

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
            image.flip(flipX, flipY), currentSpriteRect, renderRectF, null
        )
        if (playing) {
            nextFrame()
        }
    }
}
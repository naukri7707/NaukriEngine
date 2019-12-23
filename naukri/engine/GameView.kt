package naukri.engine

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.*
import androidx.appcompat.app.AppCompatActivity

class GameView(
    context: Context,
    attributes: AttributeSet
) : SurfaceView(context, attributes), SurfaceHolder.Callback {

    private val thread: GameThread

    init {
        holder.addCallback(this)
        // 生成遊戲執行緒
        thread = GameThread(holder, this)
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder?) {
        // 開始執行緒
        thread.start()
        gameInitial()
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        // no need
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                GameThread.isRunning = false
                thread.join()
                retry = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    var gameInitial = {}

    companion object {

        lateinit var applicationContext: AppCompatActivity

        lateinit var main: GameView

        var size = Vector2Int()
            private set

        val width get() = size.x

        val height  get() = size.y

        var renderCenter = Point(0, 0)
            private set

        var left = 0
            private set
        var right = 0
            private set
        var top = 0
            private set
        var bottom = 0
            private set

        @SuppressLint("ClickableViewAccessibility")
        fun startGame(
            gameView: GameView,
            context: AppCompatActivity,
            init: () -> Unit
        ) {
            // 設置靜態物件
            main = gameView
            applicationContext = context
            Object.resources = applicationContext.resources
            // 全螢幕 (隱藏 Title Bar)
            context.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            // 沉浸模式
            gameView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            // 觸碰事件
            gameView.setOnTouchListener { _, event ->
                ScreenEvent.setEvent(event)
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Collider.getOnTouchDownEvents()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        Collider.getOnTouchMoveEvents()
                    }
                    MotionEvent.ACTION_UP -> {
                        Collider.getOnTouchUpEvents()
                    }
                }
                return@setOnTouchListener true
            }
            // 外部初始化接口
            gameView.gameInitial = init
            // 啟動遊戲
            GameThread.isRunning = true
        }

        fun setResolution(width: Int, height: Int) {
            this.size.x = width
            this.size.y = height
            renderCenter = Point(width shr 1, height shr 1)
            left = -renderCenter.x
            right = renderCenter.x
            top = renderCenter.y
            bottom = -renderCenter.y
        }
    }
}
package naukri.engine

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.DisplayMetrics
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

    companion object {

        lateinit var applicationContext: AppCompatActivity

        lateinit var displayMetrics: DisplayMetrics

        lateinit var main: GameView

        var viewWidth = 0
            private set
        var viewHeight = 0
            private set
        var viewCenter = Point(0, 0)
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
            displayMetrics: DisplayMetrics,
            context: AppCompatActivity,
            init: () -> Unit
        ) {
            // 設置靜態物件
            main = gameView
            applicationContext = context
            this.displayMetrics = displayMetrics
            Object.resources = applicationContext.resources
            // 全螢幕
            context.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            // 隱藏虛擬按鍵
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
                        Collision.onTouchDown()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        Collision.onTouchMove()
                    }
                    MotionEvent.ACTION_UP -> {
                        Collision.onTouchUp()
                    }
                }
                return@setOnTouchListener true
            }
            // 設置靜態變數
            viewWidth = displayMetrics.widthPixels
            viewHeight = displayMetrics.heightPixels
            viewCenter = Point(viewWidth shr 1, viewHeight shr 1)
            left = -viewCenter.x
            right = viewCenter.x
            top = viewCenter.y
            bottom = -viewCenter.y
            // 外部初始化接口
            init()
            // 啟動遊戲
            GameThread.isRunning = true
        }
    }
}
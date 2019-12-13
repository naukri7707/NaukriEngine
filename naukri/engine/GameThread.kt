package naukri.engine

import android.view.SurfaceHolder

class GameThread(
    private val surfaceHolder: SurfaceHolder,
    private val gameView: GameView
) : Thread() {

    override fun run() {
        // 每幀開始時間
        var startTime = System.nanoTime()
        while (isRunning) {
            Render.canvas = null

            // 更新刷新時間
            Time.deltaTime = (System.nanoTime() - startTime).toFloat() / 1000000000
            Time.gameTime += Time.deltaTime
            startTime = System.nanoTime()
            try {
                // 鎖定可以被畫上的畫布
                Render.canvas = this.surfaceHolder.lockCanvas()
                // 同步本執行緒保證其他執行緒無法修改 surfaceHolder (畫布在這裡)
                synchronized(surfaceHolder) {
                    // 延時函式
                    Invoke.collision.forEach {
                        it.checkCoroutine()
                    }
                    // 啟動
                    Component.iOnEnableCollection.forEach {
                        it()
                    }
                    Component.iOnEnableCollection.clear()
                    // 初始化
                    Component.iStartCollection.forEach {
                        it()
                    }
                    Component.iStartCollection.clear()
                    // 碰撞器、觸發器更新
                    // 單次事件
                    Collider.onTouchEvents.forEach {
                        it()
                    }
                    Collider.onTouchEvents.clear()
                    // 按住 (本項不自動清除)
                    Collider.onHoldEvents.forEach {
                        it.onTouchHold()
                    }
                    // 碰撞事件
                    Collider.getOnCollisionEvents()
                    Collider.onCollisionEvents.forEach {
                        it()
                    }
                    Collider.onCollisionEvents.clear()
                    // 前行為更新
                    Behaviour.activeCollection.forEach {
                        it.iEarlyUpdate()
                    }
                    // 行為更新
                    Behaviour.activeCollection.forEach {
                        it.iUpdate()
                    }
                    // 後行為更新
                    Behaviour.activeCollection.forEach {
                        it.iLateUpdate()
                    }
                    // 繪製
                    gameView.draw(Render.canvas)
                    // 物件渲染更新
                    Render.collection.sortedWith(
                        compareBy(
                            { it.gameObject.layer },
                            { it.transform.zIndex })
                    ).forEach {
                        it.render()
                    }
                    // 關閉
                    Component.iOnDisableCollection.forEach {
                        it()
                    }
                    Component.iOnDisableCollection.clear()
                    // 移除
                    Component.iOnDestroyCollection.forEach {
                        it()
                    }
                    Component.iOnDestroyCollection.clear()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // 結束後解鎖畫布
                if (Render.canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(Render.canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    companion object {
        var isRunning = false
    }
}
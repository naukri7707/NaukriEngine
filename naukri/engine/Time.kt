package naukri.engine

class Time {
    // 靜態物件
    companion object {
        var gameTime = 0F
        var deltaTime = 0F

        // 上次更新時間
        private var prevTime = 0L

        internal fun updateTime() {
            // 更新刷新時間
            deltaTime = (System.nanoTime() - prevTime) / 1000000000F
            gameTime += deltaTime
            prevTime = System.nanoTime()
        }
    }
}
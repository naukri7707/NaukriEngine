package naukri.engine

class Coroutine(waitTime: Float, func: () -> Unit) {
    companion object {
        val collision = ArrayList<Coroutine>(128)
    }

    var isEnd = false

    private val endTime = Time.gameTime + waitTime

    private val endFunction = func

    init {
        collision.add(this)
    }

    internal fun checkCoroutine() {
        if (Time.gameTime > endTime) {
            endFunction()
            collision.remove(this)
            isEnd = true
        }
    }
}
package naukri.engine

open class Invoke(
    time: Float,
    protected val method: () -> Unit
) {
    companion object {
        val collision = ArrayList<Invoke>(128)
    }

    protected var endTime = Time.gameTime + time

    init {
        collision.add(this)
    }

    internal open fun checkCoroutine() {
        if (Time.gameTime > endTime) {
            method()
            stop()
        }
    }

    fun stop() {
        collision.remove(this)
    }
}

class InvokeRepeating(
    time: Float,
    private val repeatRate: Float,
    method: () -> Unit
) : Invoke(time, method) {
    override fun checkCoroutine() {
        if (Time.gameTime > endTime) {
            method()
            endTime += repeatRate
        }
    }
}
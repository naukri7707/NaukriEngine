package naukri.engine

class Invoke(
    time: Float,
    val method: (Invoke) -> Unit
) {
    companion object {
        val collision = ArrayList<Invoke>(128)
    }

    var repeatRate = -1F

    private var endTime = Time.gameTime + time

    constructor(time: Float, repeatRate: Float, method: (Invoke) -> Unit) : this(time, method) {
        this.repeatRate = repeatRate
    }

    init {
        collision.add(this)
    }

    internal fun checkInvoke() {
        if (Time.gameTime > endTime) {
            if (repeatRate == -1F) {
                stop()
            } else {
                endTime += repeatRate
            }
            method(this)
        }
    }

    fun reStart(time: Float) {
        collision.remove(this)
        endTime = Time.gameTime + time
        collision.add(this)
    }

    fun reStart(time: Float, repeatRate: Float) {
        collision.remove(this)
        endTime = Time.gameTime + time
        this.repeatRate = repeatRate
        collision.add(this)
    }

    fun stop() {
        collision.remove(this)
    }
}
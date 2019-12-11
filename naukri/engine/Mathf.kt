package naukri.engine

sealed class Mathf {
    companion object {
        fun clamp(value: Float, min: Float, max: Float): Float {
            return when {
                value < min -> min
                value > min -> max
                else -> value
            }
        }
    }
}
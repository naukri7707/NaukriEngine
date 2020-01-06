package naukri.engine

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import java.io.*
import kotlin.math.abs
import kotlin.random.Random


// Debug
object Assert {
    fun log(vararg msg: Any) {
        val sb = StringBuilder()
        for (m in msg) {
            sb.append(m.toString(), " , ")
        }
        Log.println(Log.ASSERT, "Log", sb.dropLast(3).toString())
    }
}

// Sprite
fun Bitmap.flip(x: Boolean, y: Boolean): Bitmap {
    val sx = if (x) -1F else 1F
    val sy = if (y) -1F else 1F
    val matrix = Matrix().apply { postScale(sx, sy, width / 2f, width / 2f) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Random.range(min: Float, max: Float): Float {
    return min + nextFloat() * (max - min)
}

fun <T> Random.value(vararg TValues: T): T {
    return TValues.random()
}

// 將 float 限制在區域內
fun Float.Companion.clamp(value: Float, min: Float, max: Float): Float {
    return when {
        value < min -> min
        value > min -> max
        else -> value
    }
}

fun Float.Companion.distance(lhs: Float, rhs: Float): Float {
    return abs(lhs - rhs)
}

// 線性插值
fun Float.Companion.lerp(from: Float, to: Float, proportion: Float): Float {
    return from - (from - to) * proportion
}

// Paint 擴充，快速設定
fun Paint.set(setFunc: (Paint) -> Unit): Paint {
    setFunc(this)
    return this
}

// Serialize

fun <T : Serializable> serialize(obj: T?): String {
    if (obj == null) {
        return ""
    }
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(obj)
    oos.close()

    return baos.toString("ISO-8859-1")
}

@Suppress("UNCHECKED_CAST")
fun <T : Serializable> deserialize(string: String): T? {
    if (string.isEmpty()) {
        return null
    }

    val bais = ByteArrayInputStream(string.toByteArray(charset("ISO-8859-1")))
    val ois = ObjectInputStream(bais)

    return ois.readObject() as T
}

@Suppress("UNCHECKED_CAST")
fun <T : Serializable> T.deepCopy(): T {
    val byteOut = ByteArrayOutputStream()
    val objOut = ObjectOutputStream(byteOut)
    objOut.writeObject(this)
    objOut.close()
    byteOut.close()
    val byteIn = ByteArrayInputStream(byteOut.toByteArray())
    val objIn = ObjectInputStream(byteIn)
    objIn.close()
    byteIn.close()
    return objIn.readObject() as T
}

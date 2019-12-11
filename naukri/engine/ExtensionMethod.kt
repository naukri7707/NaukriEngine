package naukri.engine

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import java.io.*
import java.util.*

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

// Float

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

fun <T : Serializable> deserialize(string: String): T? {
    if (string.isEmpty()) {
        return null
    }

    val bais = ByteArrayInputStream(string.toByteArray(charset("ISO-8859-1")))
    val ois = ObjectInputStream(bais)

    return ois.readObject() as T
}

fun <T : Serializable> deserialize(string: String, clazz: Class<T>): T? = deserialize<T>(string)


fun <T : Serializable> T.deepCopy(): T? {
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(this)
    oos.close()
    val bais = ByteArrayInputStream(baos.toByteArray())
    val ois = ObjectInputStream(bais)
    return ois.readObject() as T
}
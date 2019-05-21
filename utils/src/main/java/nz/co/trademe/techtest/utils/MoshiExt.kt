package nz.co.trademe.techtest.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.BufferedSource
import okio.Okio
import java.io.File
import java.lang.reflect.ParameterizedType

inline fun <reified T> load(rawResource: String, modelClass: Class<T>): T {
    val adapter = createListAdapter(modelClass)
    val source = createBufferedSource(modelClass, rawResource)
    return adapter.fromJson(source)!!
}


fun <T> createBufferedSource(clazz: Class<T>, file: String): BufferedSource? {
/*    val fixtureStreamReader = InputStreamReader(clazz.classLoader.getResourceAsStream(file))
    val charBuffer = CharArray(8 * 1024)
    val builder = StringBuffer()
    var numCharsRead: Int
    numCharsRead = fixtureStreamReader.read(charBuffer, 0, charBuffer.size)
    while (numCharsRead != -1) {
        builder.append(charBuffer, 0, numCharsRead)
    }
    val targetStream = builder.toString().byteInputStream(StandardCharsets.UTF_8)*/
    /*  val fixtureStreamReader = InputStreamReader(clazz.classLoader.getResourceAsStream(file))
      val inputStream = FileInputStream("$file")*/
    val inputStream = clazz.getClassLoader().getResourceAsStream(file)
    return Okio.buffer(Okio.source(inputStream))
}

private fun getFileFromPath(obj: Any, fileName: String): File {
    val classLoader = obj.javaClass.classLoader
    val resource = classLoader.getResource(fileName)
    return File(resource!!.path)
}


inline fun <reified T> createListAdapter(modelClass: Class<T>): JsonAdapter<T> {
    val moshi = Moshi.Builder()
        // ... add your own JsonAdapters and factories ...
        .add(KotlinJsonAdapterFactory())
        .build()
    val type = createListType(modelClass)
    val adapter = moshi.adapter<T>(T::class.java)
    return adapter
}

fun <T> createListType(modelClass: Class<T>): ParameterizedType? {
    return Types.newParameterizedType(modelClass::class.java, modelClass)
}

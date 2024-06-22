package com.example.ecometer.helper

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream

class TFLiteHelper(context: Context) {

    private var interpreter: Interpreter? = null

    init {
        interpreter = Interpreter(loadModelFile(context))
    }

    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("model2.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun predict(inputs: FloatArray): Float {
        val output = Array(1) { FloatArray(1) }
        interpreter?.run(arrayOf(inputs), output)
        return output[0][0]
    }
}

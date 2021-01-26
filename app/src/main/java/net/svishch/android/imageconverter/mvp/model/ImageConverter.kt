package net.svishch.android.imageconverter.mvp.model

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import net.svishch.android.imageconverter.mvp.model.data.ImageData
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random


class ImageConverter : ModelImageConverter {
    companion object {
        fun newInstance() = ImageConverter()
    }
    private var error : String = ""
    private var pathPng : String = ""

    // Вывод ошибки
    override fun getError(): String {
        return error
    }

    // Путь к файлу
    override fun getPathPng(): String {
        return pathPng
    }

    override fun jpgToPng(imageData: ImageData): Completable  = Completable.create { emitter ->
        convertToPng(imageData).let {
            if (it) {
                emitter.onComplete()
            } else {
                emitter.onError(RuntimeException(error))
            }
        }
    }
            .subscribeOn (Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    // Конвертация в PNG
    private fun convertToPng(imageData: ImageData) : Boolean{
        // Тестовая задержка
        try {
        Thread.sleep ( Random.nextLong ( 5000 ))
        pathPng = imageData.getPath()+"/converted.png";


            val bitmap = imageData.getBitmap()
            val convertedImage = File(pathPng)

            val fileOutputStream = FileOutputStream(convertedImage)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)

        } catch (e: Exception) {
            error = e.message.toString()
            return false
        }
        return true

    }



}
package net.svishch.android.imageconverter.mvp.model.data

import android.graphics.Bitmap

interface ImageData {
    fun getBitmap(): Bitmap?
    fun getPath(): String
}
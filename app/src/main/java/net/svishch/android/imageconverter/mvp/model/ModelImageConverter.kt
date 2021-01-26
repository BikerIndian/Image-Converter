package net.svishch.android.imageconverter.mvp.model

import io.reactivex.rxjava3.core.Completable
import net.svishch.android.imageconverter.mvp.model.data.ImageData

interface ModelImageConverter {
    fun jpgToPng(imageData: ImageData): Completable
    fun getError():String
    fun getPathPng():String
}
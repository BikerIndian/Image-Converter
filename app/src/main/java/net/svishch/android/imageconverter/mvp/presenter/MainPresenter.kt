package net.svishch.android.imageconverter.mvp.presenter

import android.os.Handler
import android.os.Looper
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import net.svishch.android.imageconverter.mvp.model.ModelImageConverter
import net.svishch.android.imageconverter.mvp.model.data.ImageData
import net.svishch.android.imageconverter.mvp.view.MainView


class MainPresenter(val router: Router, var imageConverter: ModelImageConverter) : MvpPresenter<MainView>() {

    var disposable : Disposable? = null

    fun backClicked() {
        router.exit()
    }

    fun  selectImageButtonClick(){
        getViewState().imageSelect()
    }

    fun  startButtonClick(imageData: ImageData){
        //Конвертируем кортинку и отправляем в MainActivity

        try {
            disposable = imageConverter.jpgToPng(imageData).subscribe({
                getViewState().setImagePng(imageConverter.getPathPng())
            }, {
                getViewState().setError(it.message)
            })

        }
            catch (e: Exception) {
                getViewState().setError(e.message)
            }

    }

    fun stop() {
        this.disposable?.dispose()
    }


}
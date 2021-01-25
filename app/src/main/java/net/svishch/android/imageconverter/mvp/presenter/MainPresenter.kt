package net.svishch.android.imageconverter.mvp.presenter

import moxy.MvpPresenter
import net.svishch.android.imageconverter.mvp.view.MainView
import ru.terrakok.cicerone.Router

class MainPresenter(val router: Router) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
    }

    fun backClicked() {
        router.exit()
    }

}
package net.svishch.android.imageconverter.ui

import android.os.Bundle
import moxy.MvpAppCompatActivity
import moxy.MvpFacade.init
import moxy.ktx.moxyPresenter
import net.svishch.android.imageconverter.App
import net.svishch.android.imageconverter.R
import net.svishch.android.imageconverter.mvp.presenter.MainPresenter
import net.svishch.android.imageconverter.mvp.view.MainView
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class MainActivity : MvpAppCompatActivity(), MainView {
    val navigatorHolder = App.instance.navigatorHolder

    // R.id.container - контейнер для фрагментов
    val navigator = SupportAppNavigator(this, supportFragmentManager, R.id.container)

    private val presenter by moxyPresenter {
        MainPresenter(App.instance.router)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {

        supportFragmentManager.fragments.forEach {
            if(it is BackButtonListener && it.backPressed()){
                return
            }
        }

        presenter.backClicked()
    }
}
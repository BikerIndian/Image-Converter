package net.svishch.android.imageconverter.ui

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import net.svishch.android.imageconverter.App
import net.svishch.android.imageconverter.R
import net.svishch.android.imageconverter.mvp.model.ImageConverter
import net.svishch.android.imageconverter.mvp.model.data.ImageData
import net.svishch.android.imageconverter.mvp.presenter.MainPresenter
import net.svishch.android.imageconverter.mvp.view.MainView
import java.io.InputStream


class MainActivity : MvpAppCompatActivity(), MainView {
    companion object {
        private const val REQUEST_IMAGE_OPEN = 1
        private const val IMAGE_RESULT_CODE = "net.svishch.android.imageconverter.ui.MainActivity"
    }

    private val navigatorHolder = App.instance.navigatorHolder
    private var imageData = ImageDataModel.newInstance()

    private val presenter by moxyPresenter {
        MainPresenter(App.instance.router, ImageConverter.newInstance())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // События на кнопки
        btn_select.setOnClickListener { presenter.selectImageButtonClick() }
        btn_start.setOnClickListener {
            presenter.startButtonClick(imageData)
            // Очиска imageView для PNG картинки
            clearImagePng()
            stop_ON()

        }
        btn_stop.setOnClickListener {
            presenter.stop()
            start_ON()
        }

    }

    fun stop_ON() {
        btn_stop.setVisibility(View.VISIBLE);
        btn_start.setVisibility(View.GONE)
    }

    fun start_ON() {
        btn_start.setVisibility(View.VISIBLE)
        btn_stop.setVisibility(View.GONE);
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is BackButtonListener && it.backPressed()) {
                return
            }
        }
        presenter.backClicked()
    }

    // Выбор картинки
    override fun imageSelect() {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, IMAGE_RESULT_CODE), REQUEST_IMAGE_OPEN)
        start_ON()
    }

    // Устанавливаем PNG картинку
    override fun setImagePng(pathPng: String) {
        imageConvert.setImageURI(Uri.parse(pathPng))

        start_ON()
    }

    // Вывод ошибки
    override fun setError(message: String?) {
        Toast.makeText(this, getApplicationContext().getString(R.string.error) + " $message", Toast.LENGTH_LONG).show()
    }


    // Загрузка кортинки на View
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            var imageUri: Uri?
            data?.let {
                imageUri = data.data

                // Устанавливаем JPG картинку
                imageUri?.let {
                    imageViewJpg.setImageURI(imageUri)
                    imageData.setParam(imageUri, contentResolver, getApplicationContext())
                }
            }

        }

        // Очиска imageView для PNG картинки
        clearImagePng()


    }

    private fun clearImagePng() {
        imageConvert.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_crop_original_24)
        )
    }

    // Для передачи данных в модель
    class ImageDataModel : ImageData {
        private var bitmap: Bitmap? = null
        private var pathApp: String = ""

        companion object {
            fun newInstance() = ImageDataModel()
        }

        fun setParam(imageUri: Uri?, contentResolver: ContentResolver, context: Context) {

            val steamIn: InputStream? = imageUri?.let { contentResolver.openInputStream(it) }
            bitmap = BitmapFactory.decodeStream(steamIn)
            steamIn?.close()

            pathApp = (context.getExternalFilesDir(Environment.DIRECTORY_PICTURES))?.getPath().toString()
        }


        override fun getBitmap(): Bitmap? {
            return bitmap;
        }

        override fun getPath(): String {
            return pathApp
        }
    }
}



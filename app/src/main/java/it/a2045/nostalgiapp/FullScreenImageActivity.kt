package it.a2045.nostalgiapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.activity_full_screen_image.*

class FullScreenImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        Glide.with(this)
            .load(intent.getStringExtra(IMAGE_URL_EXTRA))
            .into(iv_full_screen_image)
    }

    companion object {
        val IMAGE_URL_EXTRA = "IMAGE_URL_EXTRA"
    }
}

package it.a2045.nostalgiapp

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.activity_full_screen_image.*

class FullScreenImageActivity : AppCompatActivity() {

    private val mMediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        Glide.with(this)
            .load(intent.getStringExtra(IMAGE_URL_EXTRA))
            .into(iv_full_screen_image)

        fab.setOnClickListener {
            playAudio()
        }
    }

    companion object {
        val IMAGE_URL_EXTRA = "IMAGE_URL_EXTRA"
        val AUDIO_URL_EXTRA = "AUDIO_URL_EXTRA"
    }

    private fun playAudio() {

        mMediaPlayer.stop()
        try {
            mMediaPlayer.reset()
            mMediaPlayer.setDataSource(intent.getStringExtra(AUDIO_URL_EXTRA))
            mMediaPlayer.prepareAsync()
            mMediaPlayer.setOnPreparedListener {
                it.start()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "The file does not exist", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPause() {
        mMediaPlayer.stop()
        super.onPause()
    }

}

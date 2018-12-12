package it.a2045.nostalgiapp

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView


class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val mGif = findViewById<GifImageView>(R.id.iv_luigina)


        val mMediaPlayer = MediaPlayer()
        try {
            mMediaPlayer.setDataSource(
                this,
                Uri.parse("android.resource://" + this@SplashActivity.packageName + "/raw/luigina_intro")
            )
            mMediaPlayer.prepareAsync()
            mMediaPlayer.setOnPreparedListener {
                val gifDrawable = GifDrawable(resources, R.drawable.luigina_gif)
                mGif.setImageDrawable(gifDrawable)
                gifDrawable.stop()
                gifDrawable.loopCount = 1
                gifDrawable.start()
                it.start()
            }
            mMediaPlayer.setOnCompletionListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        } catch (e: Exception) {
            Toast.makeText(this, "The file does not exist", Toast.LENGTH_LONG).show()
        }
    }

}
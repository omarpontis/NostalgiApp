package it.a2045.nostalgiapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import it.a2045.nostalgiapp.models.Collega
import it.a2045.nostalgiapp.models.FotoParlante
import it.a2045.nostalgiapp.models.RicordoUfficio
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlin.random.Random
import kotlin.random.nextInt

fun Context.toastLong(testo:String) {Toast.makeText(this, testo, Toast.LENGTH_LONG).show()}

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    ExColleghiFragment.OnListFragmentInteractionListener,
    FotoParlanteFragment.OnFotoParlanteInteractionListener,
    RicordiUfficioFragment.OnRicordiUfficioFragmentInteractionListener,
    InfoTrafficoFragment.OnInfoTrafficoInteractionListener {

    private val URLJson = "https://zeppelin.iptvng.eu.org/embedded/nsapp/config.json"
    private val mMediaPlayer = MediaPlayer()
    lateinit var mSnackbar: Snackbar
    var mInfoTrafficoIndex = -1

    var mListaColleghi: List<Collega>? = null
        private set
    var mListaRicordi: List<RicordoUfficio>? = null
        private set
    var mFotoParlante: FotoParlante? = null
        private set
    var mListaInfoTraffico: List<String>? = null
        private set

    lateinit var dialog: AlertDialog

    private val TAG = MainActivity::class.qualifiedName

    private val mExColleghiFragment : ExColleghiFragment = ExColleghiFragment.newInstance()
    private val mFotoParlanteFragment : FotoParlanteFragment = FotoParlanteFragment.newInstance()
    private val mRicordiUfficioFragment : RicordiUfficioFragment = RicordiUfficioFragment.newInstance()
    private val mInfoTrafficoFragment : InfoTrafficoFragment = InfoTrafficoFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        nav_view.getHeaderView(0).findViewById<ImageView>(R.id.iv_luigina).setOnClickListener {
            playAudio(
                null,
                Uri.parse("android.resource://" + this@MainActivity.packageName + "/raw/luigina_impossibile")
            )
        }

        getRequest()
    }

    private fun getRequest() {
        showProgressDialog()
        Thread {
            URLJson.httpGet()
                .responseJson { _, _, result ->
                    val (json, error) = result
                    dialog.dismiss()
                    if (json != null) {
                        parseJson(json.content)
                        onNavigationItemSelected(nav_view.menu.getItem(0))
                    } else {
                        showAlert(error?.exception?.message)
                    }
                }
        }.start()
    }

    private fun parseJson(jsonString: String) {
//        val data: JsonObject = Parser().parse(assets.open("config.json")) as JsonObject
        val data: JsonObject = Parser().parse(jsonString.byteInputStream()) as JsonObject

        @Suppress("UNCHECKED_CAST")
        val sezioni = data["sezioni"] as JsonArray<JsonObject>

        val klaxon = Klaxon()
        var listarray: JsonArray<JsonObject> = JsonArray()
        var contenuto: JsonObject

        sezioni.forEach {
            contenuto = it["contenuto"] as JsonObject
            if (contenuto.contains("list_array")) {
                @Suppress("UNCHECKED_CAST")
                listarray = contenuto["list_array"] as JsonArray<JsonObject>
            }

            when (it["nome"]) {

                "ex_colleghi" -> {
                    mListaColleghi = klaxon.parseFromJsonArray(listarray)
                    Log.d(TAG, "listaColleghi   $mListaColleghi")
                }
                "foto_parlante" -> {
                    mFotoParlante = klaxon.parseFromJsonObject(contenuto)
                    Log.d(TAG, "fotoParlante   $mFotoParlante")
                }
                "ricordi_ufficio" -> {
                    mListaRicordi = klaxon.parseFromJsonArray(listarray)
                    Log.d(TAG, "listaRicordi   $mListaRicordi")
                }
                "info_traffico" -> {
                    mListaInfoTraffico = klaxon.parseFromJsonArray(listarray)
                    Log.d(TAG, "infoTraffico   $mListaInfoTraffico")
                }
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
//        } else if (supportFragmentManager.backStackEntryCount ) {
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_miei_ex_colleghi -> {
                selectItem(mExColleghiFragment)
            }
            R.id.nav_foto_parlante -> {
                selectItem(mFotoParlanteFragment)
            }
            R.id.nav_ricordi_ufficio -> {
                selectItem(mRicordiUfficioFragment)
            }
            R.id.nav_oggi_esco_presto -> {
                selectItem(mInfoTrafficoFragment)
            }
        }
        title = item.title
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    @SuppressLint("CommitTransaction") // commit() is called
    private fun selectItem(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.content_frame, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun playAudio(path: String?, uri: Uri?) {

        stopAudio()
        try {
            mMediaPlayer.reset()
            if (path != null) {
                mMediaPlayer.setDataSource(path)
            } else if (uri != null) {
                mMediaPlayer.setDataSource(this, uri)
            }
            mMediaPlayer.prepareAsync()
            mMediaPlayer.setOnPreparedListener {
                it.start()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "The file does not exist", Toast.LENGTH_LONG).show()
        }
    }

    private fun startVideoPlayer(video: String?) {
        val videoPlayerIntent = Intent(this, VideoPlayerActivity::class.java)
        videoPlayerIntent.putExtra(VideoPlayerActivity.VIDEO_URL_EXTRA, video)
        startActivity(videoPlayerIntent)
    }

    override fun stopAudio() {
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.stop()
        }
    }

    override fun onFabClick(audio: String?) {
        playAudio(audio, null)
    }

    override fun onListFragmentInteraction(item: Collega?) {
        playAudio(item?.audio, null)
    }

    override fun onRicordoUfficioInteraction(item: RicordoUfficio?) {
        if (!TextUtils.isEmpty(item?.video))
            startVideoPlayer(item?.video)
        else
            playAudio(item?.audio, null)
    }

    override fun onShowInfoTraffico() {
        if (mListaInfoTraffico != null) {
            val listSize = mListaInfoTraffico!!.size
            if (mInfoTrafficoIndex == -1) {
                mInfoTrafficoIndex = Random.nextInt(0, listSize.minus(1))
            }
            mSnackbar = Snackbar.make(findViewById(android.R.id.content), mListaInfoTraffico!!.get(mInfoTrafficoIndex), Snackbar.LENGTH_INDEFINITE)
            mSnackbar.show()
            mInfoTrafficoIndex = mInfoTrafficoIndex.plus(1).rem(listSize)
        }
    }

    override fun onDismissInfoTraffico() {
        mSnackbar?.dismiss()
    }

    private fun showAlert(message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Errore")
        builder.setMessage(message)

        builder.setPositiveButton("OK") { _, _ ->
        }
        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    private fun showProgressDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

}
package it.a2045.nostalgiapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import it.a2045.nostalgiapp.models.Collega
import it.a2045.nostalgiapp.models.FotoParlante
import it.a2045.nostalgiapp.models.RicordoUfficio
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    ExColleghiFragment.OnListFragmentInteractionListener {

    var mListaColleghi: List<Collega>? = null
        private set
    var mListaRicordi: List<RicordoUfficio>? = null
        private set
    var mFotoParlante: FotoParlante? = null
        private set

    private val TAG = MainActivity::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        parseJson()
        selectItem(ExColleghiFragment.newInstance())

    }

    private fun parseJson() {
        val data: JsonObject = Parser().parse(assets.open("config.json")) as JsonObject
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
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_miei_ex_colleghi -> {
                selectItem(ExColleghiFragment.newInstance())
            }
            R.id.nav_foto_parlante -> {
                selectItem(FotoParlanteFragment.newInstance())
            }
            R.id.nav_ricordi_ufficio -> {
                selectItem(RicordiUfficioFragment.newInstance())
            }
            R.id.nav_oggi_esco_presto -> {
                selectItem(MappaFragment.newInstance())
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    @SuppressLint("CommitTransaction") // commit() is called
    private fun selectItem(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.content_frame, fragment)
            commit()
        }
    }

    override fun onListFragmentInteraction(item: Collega?) {
        toast("ITEM ${item?.nome}\n${item?.testo}")
    }

}
package it.a2045.nostalgiapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
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
import it.a2045.nostalgiapp.models.DummyContent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.InputStream

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    ExColleghiFragment.OnListFragmentInteractionListener {

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

        val parser = Parser()

        val data : JsonObject = parser.parse(assets.open("config.json")) as JsonObject
        val sezioni : JsonArray<JsonObject> = data["sezioni"] as JsonArray<JsonObject>


        for((index,obj) in sezioni.withIndex()) {
            println("Loop Iteration $index on each object")
            if("ex_colleghi".equals(obj["nome"])){
                val contenuto : JsonObject = obj["contenuto"] as JsonObject
                val collegaArray : JsonArray<JsonObject> = contenuto["list_array"] as JsonArray<JsonObject>
                Log.d(TAG, "collegaarray   $collegaArray")
            }

        }

//        for (i in 0..(sezioni.length() - 1)) {
//            val sezione = sezioni.getJSONObject(i)

//        val result = Klaxon()
//            .parse<Collega>(readJSONFromAsset())
//



//        var jsonObject = JSONObject(readJSONFromAsset())
//        Log.d(TAG, "jsonObject\n$jsonObject ")
//
//        var sezioni = jsonObject.getJSONArray("sezioni")
//
//        for (i in 0..(sezioni.length() - 1)) {
//            val sezione = sezioni.getJSONObject(i)

//            when (sezione.get("nome")) {
//                "ex_colleghi" -> sezione.get("contenuto").
//                "foto_parlante" -> Log.d(TAG, "foto_parlante")
//                "ricordi_ufficio" -> Log.d(TAG, "ricordi_ufficio")
//                else -> Log.d(TAG, "ALTRO!")
//            }

//        }

//        var jsonArray = Gson().fromJson(sampleJson, Collega::class.java)
//
//        for (jsonIndex in 0..(jsonArray.size() - 1)) {
//            Log.d("JSON2", jsonArray.getJSONObject(jsonIndex).getString("title"))
//        }
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
            }
            R.id.nav_ricordi_ufficio -> {
            }
            R.id.nav_oggi_esco_presto -> {
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

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        toast("STO CAZZO!!")
    }

    fun readJSONFromAsset(): String {
        var json = ""
        try {
            val inputStream: InputStream = assets.open("config.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ""
        }
        return json
    }
}
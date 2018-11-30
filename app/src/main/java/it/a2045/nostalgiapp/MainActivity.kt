package it.a2045.nostalgiapp

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import it.a2045.nostalgiapp.models.Collega
import it.a2045.nostalgiapp.models.FotoParlante
import it.a2045.nostalgiapp.models.RicordoUfficio
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    ExColleghiFragment.OnListFragmentInteractionListener, OnMapReadyCallback,
    FotoParlanteFragment.OnFotoParlanteInteractionListener,
    RicordiUfficioFragment.OnRicordiUfficioFragmentInteractionListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment

    private val URLJson = "https://zeppelin.iptvng.eu.org/embedded/nsapp/config.json"
    private val mMediaPlayer = MediaPlayer()

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

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        getRequest()
    }

    private fun getRequest() {
        URLJson.httpGet()
            .responseJson { _, _, result ->
                val (json, error) = result
                if (json != null) {
                    parseJson(json.content)
                    selectItem(ExColleghiFragment.newInstance())
                } else {
                    showAlert(error?.exception?.message)
                }
            }
    }

    private fun parseJson(jsonString: String) {
        val data: JsonObject = Parser().parse(assets.open("config.json")) as JsonObject
//        val data: JsonObject = Parser().parse(jsonString.byteInputStream()) as JsonObject

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
//        } else if (supportFragmentManager.backStackEntryCount ) {
        } else {
            super.onBackPressed()
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
                mapFragment = SupportMapFragment.newInstance()
                mapFragment.getMapAsync(this)

                selectItem(mapFragment)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    @SuppressLint("CommitTransaction") // commit() is called
    private fun selectItem(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.content_frame, fragment)
//            addToBackStack(null)
            commit()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        val torino = LatLng(45.069074, 7.686587)
        val casaLuigina = LatLng(45.002732, 7.659876)
        val lavoro = LatLng(45.112143, 7.6761608)
        mMap.addMarker(MarkerOptions().position(casaLuigina).title("Casa"))
        mMap.addMarker(MarkerOptions().position(lavoro).title("T-Lab"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(torino))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(casaLuigina, 5f))

        getDirectionURL(casaLuigina, lavoro).httpGet()
            .responseString { request, response, result ->
                Log.d(TAG, "omarMap result: ${result}")
                Log.d(TAG, "omarMap request: ${request}")
                Log.d(TAG, "omarMap response: ${response}")
//                when (result) {
//                    is Result -> {
//                        val ex = result.getException()
//                    }
//                    is Result.Success -> {
//                        val data = result.get()
//                    }
//                }
            }

    }

    fun playAudio(path: String?) {

        stopAudio()
        try {
            mMediaPlayer.reset()
            mMediaPlayer.setDataSource(path)
            mMediaPlayer.prepareAsync()
            mMediaPlayer.setOnPreparedListener {
                it.start()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "The file does not exist", Toast.LENGTH_LONG).show()
        }
    }

    override fun stopAudio() {
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.stop()
        }
    }

    override fun onFabClick(audio: String?) {
        playAudio(audio)
    }

    override fun onListFragmentInteraction(item: Collega?) {
        playAudio(item?.audio)
    }

    override fun onRicordoUfficioInteraction(item: RicordoUfficio?) {
        playAudio(item?.audio)
    }

    fun getDirectionURL(origin: LatLng, dest: LatLng): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&sensor=false&mode=driving&key=${getString(
            R.string.google_maps_key
        )}"
    }

//    private inner class GetDirection(val url : String) : AsyncTask<Void,Void,List<List<LatLng>>>(){
//        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
//            val client = OkHttpClient()
//            val request = Request.Builder().url(url).build()
//            val response = client.newCall(request).execute()
//            val data = response.body()!!.string()
//            Log.d("GoogleMap" , " data : $data")
//            val result =  ArrayList<List<LatLng>>()
//            try{
//                val respObj = Gson().fromJson(data,GoogleMapDTO::class.java)
//
//                val path =  ArrayList<LatLng>()
//
//                for (i in 0..(respObj.routes[0].legs[0].steps.size-1)){
////                    val startLatLng = LatLng(respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble()
////                            ,respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble())
////                    path.add(startLatLng)
////                    val endLatLng = LatLng(respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble()
////                            ,respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble())
//                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
//                }
//                result.add(path)
//            }catch (e:Exception){
//                e.printStackTrace()
//            }
//            return result
//        }
//
//        override fun onPostExecute(result: List<List<LatLng>>) {
//            val lineoption = PolylineOptions()
//            for (i in result.indices){
//                lineoption.addAll(result[i])
//                lineoption.width(10f)
//                lineoption.color(Color.BLUE)
//                lineoption.geodesic(true)
//            }
//            googleMap.addPolyline(lineoption)
//        }
//    }

    public fun decodePolyline(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }

        return poly
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

}
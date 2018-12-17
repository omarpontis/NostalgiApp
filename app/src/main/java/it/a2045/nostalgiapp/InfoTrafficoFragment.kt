package it.a2045.nostalgiapp


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlin.concurrent.thread
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable



class InfoTrafficoFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    private var listener: OnInfoTrafficoInteractionListener? = null
    var showNextInfo = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_traffico, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.fr_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnInfoTrafficoInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnInfoTrafficoInteractionListener")
        }
        showNextInfo = true
        thread(start = true) {
            while (showNextInfo) {
                listener?.onShowInfoTraffico()
                Thread.sleep(6000)
                listener?.onDismissInfoTraffico()
                Thread.sleep(1500)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (listener as MainActivity).title = resources.getString(R.string.info_traffico)
    }

    override fun onPause() {
        showNextInfo = false
        listener?.onDismissInfoTraffico()
        super.onPause()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        val centerCamera = LatLng(45.068873, 7.638482)
        val casaLuigina = LatLng(45.0021566, 7.658212)
        val lavoro = LatLng(45.1124765, 7.670353700000001)

        val height = 400
        val width = 400
        val bdCasaLuigina = activity?.getDrawable(R.drawable.casaluigina) as BitmapDrawable
        val bCasaLuigina = bdCasaLuigina.bitmap
        val casaLuiginaMarker = Bitmap.createScaledBitmap(bCasaLuigina, width, height, false)

        val bdTilab = activity?.getDrawable(R.drawable.tilabmarker) as BitmapDrawable
        val bTilab = bdTilab.bitmap
        val tilabMarker = Bitmap.createScaledBitmap(bTilab, width, height, false)

        mMap.addMarker(MarkerOptions().position(casaLuigina).title("Casa").icon(BitmapDescriptorFactory.fromBitmap(casaLuiginaMarker)))
        mMap.addMarker(MarkerOptions().position(lavoro).title("TILab").icon(BitmapDescriptorFactory.fromBitmap(tilabMarker)))
        drawPolylines()
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerCamera, 11f))
    }

    private fun drawPolylines() {

        val respObj: JsonObject = Parser().parse(activity!!.assets.open("route.json")) as JsonObject
        @Suppress("UNCHECKED_CAST")
        val steps = respObj["steps"] as JsonArray<JsonObject>

        var polyline: JsonObject
        val path = ArrayList<LatLng>()

        steps.forEach {
            polyline = it["polyline"] as JsonObject
            path.addAll(decodePolyline(polyline["points"] as String))
        }
        val lineOption = PolylineOptions()
        lineOption.addAll(path)
        lineOption.width(15f)
        lineOption.color(R.color.colorPrimary)
        lineOption.geodesic(true)
        mMap.addPolyline(lineOption)
    }

    private fun decodePolyline(encoded: String): List<LatLng> {

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

    interface OnInfoTrafficoInteractionListener {
        fun onShowInfoTraffico()
        fun onDismissInfoTraffico()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InfoTrafficoFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}

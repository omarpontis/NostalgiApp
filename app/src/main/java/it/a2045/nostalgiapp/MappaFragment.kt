package it.a2045.nostalgiapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_mappa.*
import it.a2045.nostalgiapp.R.id.mapView
import com.google.android.gms.maps.MapView



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MappaFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MappaFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MappaFragment : Fragment(), OnMapReadyCallback {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = (view.findViewById(R.id.map) as MapView).toInt()
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        // Gets to GoogleMap from the MapView and does initialization stuff
        mMap = map_view.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mappa, container, false)

        // Gets to GoogleMap from the MapView and does initialization stuff
        mMap = map_view.getMapAsync(this)


        mMap.getUiSettings().setMyLocationButtonEnabled(false)
//        mMap.setMyLocationEnabled(true)

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this.activity!!)
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }


        // Updates the location and zoom of the MapView
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(43.1, -87.9), 10f)
        mMap.animateCamera(cameraUpdate)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MappaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            MappaFragment().apply {
                //                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }



    override fun onMapReady(map: GoogleMap) {
        System.err.println("OnMapReady start")
        mMap = map as GoogleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Seed nay"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//        Toast.makeText(this.context, "OnMapReady end", Toast.LENGTH_LONG).show()
    }
}



//object: OnMapReadyCallback{
//    override fun onMapReady(p0: GoogleMap?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//}

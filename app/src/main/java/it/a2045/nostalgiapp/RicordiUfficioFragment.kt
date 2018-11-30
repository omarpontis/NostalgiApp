package it.a2045.nostalgiapp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.a2045.nostalgiapp.models.RicordoUfficio

class RicordiUfficioFragment : Fragment() {

    private var listener: OnRicordiUfficioFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ricordi_ufficio, container, false)

        if (view is RecyclerView) {
            with(view) {
                layoutManager = GridLayoutManager(context,2)
                setHasFixedSize(true)
                adapter = RicordiUfficioAdapter(listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnRicordiUfficioFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnRicordiUfficioFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnRicordiUfficioFragmentInteractionListener {
        fun onRicordoUfficioInteraction(item: RicordoUfficio?)
        fun stopAudio()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RicordiUfficioFragment().apply {}
    }

    override fun onPause() {
        listener?.stopAudio()
        super.onPause()
    }

}

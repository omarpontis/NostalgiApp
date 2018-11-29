package it.a2045.nostalgiapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import it.a2045.nostalgiapp.models.Collega
import kotlinx.android.synthetic.main.fragment_foto_parlante.*
import kotlinx.android.synthetic.main.fragment_foto_parlante.view.*
import java.net.URI

class FotoParlanteFragment : Fragment() {

    private var listener: OnFabInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFabInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFabInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener {
            listener?.onFabClick((listener as MainActivity).mFotoParlante?.audio)
        }

        Glide.with(listener as Context)
            .load((listener as MainActivity).mFotoParlante?.foto)
            .into(view.iv_foto_parlante)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_foto_parlante, container, false)
    }

    interface OnFabInteractionListener {
        fun onFabClick(audio: String?)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FotoParlanteFragment().apply {}
    }
}

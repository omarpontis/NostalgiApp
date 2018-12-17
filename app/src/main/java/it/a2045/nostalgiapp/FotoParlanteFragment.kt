package it.a2045.nostalgiapp

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_foto_parlante.*
import kotlinx.android.synthetic.main.fragment_foto_parlante.view.*

class FotoParlanteFragment : Fragment() {

    private var listener: OnFotoParlanteInteractionListener? = null
    private var mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mShortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFotoParlanteInteractionListener) {
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

        iv_foto_parlante.setOnClickListener {
            startFullScreenImage((listener as MainActivity).mFotoParlante?.foto)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_foto_parlante, container, false)
    }

    interface OnFotoParlanteInteractionListener {
        fun onFabClick(audio: String?)
        fun stopAudio()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FotoParlanteFragment().apply {}
    }

    override fun onPause() {
        listener?.stopAudio()
        super.onPause()
    }

    private fun startFullScreenImage(image: String?) {
        val intent = Intent(listener as Activity, FullScreenImageActivity::class.java)
        intent.putExtra(FullScreenImageActivity.IMAGE_URL_EXTRA, image)
        startActivity(intent)
    }
}

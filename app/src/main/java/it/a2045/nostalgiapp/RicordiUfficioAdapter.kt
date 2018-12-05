package it.a2045.nostalgiapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import it.a2045.nostalgiapp.RicordiUfficioFragment.OnRicordiUfficioFragmentInteractionListener
import it.a2045.nostalgiapp.models.RicordoUfficio
import kotlinx.android.synthetic.main.fragment_ricordi_ufficio_item.view.*

class RicordiUfficioAdapter(
    private val mListener: OnRicordiUfficioFragmentInteractionListener?
) : RecyclerView.Adapter<RicordiUfficioAdapter.ViewHolder>() {

    private var mValues: List<RicordoUfficio>? = (mListener as MainActivity).mListaRicordi
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as RicordoUfficio
            mListener?.onRicordoUfficioInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_ricordi_ufficio_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues!![position]

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_foreground)

        Glide.with(mListener as Context)
            .load(item.foto)
            .apply(requestOptions)
            .into(holder.mIvFoto)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIvFoto: ImageView = mView.iv_ricordo

    }
}

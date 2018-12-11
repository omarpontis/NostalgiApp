package it.a2045.nostalgiapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


import it.a2045.nostalgiapp.ExColleghiFragment.OnListFragmentInteractionListener
import it.a2045.nostalgiapp.models.Collega

import kotlinx.android.synthetic.main.fragment_ex_colleghi_item.view.*
import android.graphics.Typeface


class ExColleghiAdapter(
    private val mListener: OnListFragmentInteractionListener?,
    private val mPhotoListener: OnPhotoClickListener?
) : RecyclerView.Adapter<ExColleghiAdapter.ViewHolder>() {

    interface OnPhotoClickListener {
        fun zoomImageFromThumb(thumbView: View, urlImage: String)
    }

    private var mValues: List<Collega>? = (mListener as MainActivity).mListaColleghi

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_ex_colleghi_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues!![position]
        holder.mTvName.text = item.nome
        holder.mTvMessage.text = item.testo

        holder.mIvIcona.setOnClickListener { v ->
            mListener?.onListFragmentInteraction(v.tag as Collega)
        }
        holder.mIvFoto.setOnClickListener { v ->
            mPhotoListener?.zoomImageFromThumb(v, item.foto)
        }

        Glide.with(mListener as Context)
            .load(item.foto)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.mIvFoto)

        with(holder.mIvIcona) {
            tag = item
        }
    }

    override fun getItemCount(): Int = mValues?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIvFoto: ImageView = mView.iv_foto
        val mTvName: TextView = mView.tv_name
        val mTvMessage: TextView = mView.tv_message
        val mIvIcona: ImageView = mView.iv_icona

        override fun toString(): String {
            return super.toString() + " '" + mTvName.text + "' " + mTvMessage.text + "'"
        }
    }
}

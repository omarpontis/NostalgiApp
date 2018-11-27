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

class ExColleghiAdapter(
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<ExColleghiAdapter.ViewHolder>() {

    private var mValues: List<Collega>? = (mListener as MainActivity).mListaColleghi
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Collega
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_ex_colleghi_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues!![position]
        holder.mTvName.text = item.nome
        holder.mTvMessage.text = item.testo

        Glide.with(mListener as Context)
            .load(item.foto)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.mIvFoto)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues!!.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIvFoto: ImageView = mView.iv_foto
        val mTvName: TextView = mView.tv_name
        val mTvMessage: TextView = mView.tv_message

        override fun toString(): String {
            return super.toString() + " '" + mTvName.text + "' " + mTvMessage.text + "'"
        }
    }
}

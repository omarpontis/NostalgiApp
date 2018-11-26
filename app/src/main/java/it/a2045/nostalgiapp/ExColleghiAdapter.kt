package it.a2045.nostalgiapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import it.a2045.nostalgiapp.ExColleghiFragment.OnListFragmentInteractionListener
import it.a2045.nostalgiapp.models.Collega
import it.a2045.nostalgiapp.models.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_ex_colleghi_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
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
//        holder.mIvFoto
        holder.mTvName.text = item.nome
        holder.mTvMessage.text = item.testo
//    holder.mIvIcona


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
        val mIvIcona: ImageView = mView.iv_icona

        override fun toString(): String {
            return super.toString() + " '" + mTvName.text + "' " + mTvMessage.text + "'"
        }
    }
}

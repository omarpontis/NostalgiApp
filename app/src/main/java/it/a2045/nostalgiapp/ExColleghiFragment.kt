package it.a2045.nostalgiapp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.a2045.nostalgiapp.models.Collega

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ExColleghiFragment.OnListFragmentInteractionListener] interface.
 */
class ExColleghiFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        arguments?.let {
//            columnCount = it.getInt(ARG_COLUMN_COUNT)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ex_colleghi, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
//                layoutManager = when {
//                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount)
//                }

                adapter = ExColleghiAdapter(listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Collega?)
    }

    companion object {

//            const val ARG_COLLEGHI_LIST = "colleghi_list"

        @JvmStatic
        fun newInstance() =
            ExColleghiFragment().apply {
//                arguments = Bundle().apply {
//                    putParcelableArrayList(ARG_COLLEGHI_LIST, listaColleghi)
//                }
            }

    }
}

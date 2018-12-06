package it.a2045.nostalgiapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_video_player.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_VIDEO_URL = "videoUrl"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [VideoPlayerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [VideoPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class VideoPlayerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var videoUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoUrl = it.getString(ARG_VIDEO_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureVideoView()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VideoPlayerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?) =
            VideoPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_VIDEO_URL, param1)
                }
            }
    }

    private fun configureVideoView() {
        if (!TextUtils.isEmpty(videoUrl) && videoUrl!!.startsWith("http")) {
            videoView1.setVideoPath(videoUrl)
            videoView1.start()
        } else {
            toastLong("Url video non valido")
        }
    }
}

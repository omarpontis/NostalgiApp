package it.a2045.nostalgiapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import it.a2045.nostalgiapp.models.Collega
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.fragment_ex_colleghi.*


class ExColleghiFragment : Fragment(), ExColleghiAdapter.OnPhotoClickListener {

    private var listener: OnListFragmentInteractionListener? = null
    private var mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0
    private var mFLBlur: FrameLayout? = null
    private var mRv: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mShortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ex_colleghi, container, false)

        mFLBlur = view.findViewById(R.id.fl_blur)
        mRv = view.findViewById<RecyclerView>(R.id.rv_ex_colleghi)
        mRv?.adapter = ExColleghiAdapter(listener, this@ExColleghiFragment)

        val resId = R.anim.layout_animation_fall_down
        val animation = AnimationUtils.loadLayoutAnimation(listener as MainActivity, resId)
        mRv?.layoutAnimation = animation

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

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Collega?)
        fun stopAudio()
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ExColleghiFragment().apply {}
    }


    override fun onResume() {
        super.onResume()
        (listener as MainActivity).title = resources.getString(R.string.miei_ex_colleghi)
    }

    override fun onPause() {
        listener?.stopAudio()
        super.onPause()
    }

    override fun zoomImageFromThumb(thumbView: View, urlImage: String) {
        mCurrentAnimator?.cancel()

        Glide.with(activity as Context)
            .load(urlImage)
            .into(iv_expanded_image)

        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        thumbView.getGlobalVisibleRect(startBoundsInt)
        container.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        thumbView.alpha = 0f
        iv_expanded_image.visibility = View.VISIBLE

        iv_expanded_image.pivotX = 0f
        iv_expanded_image.pivotY = 0f

        mCurrentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    iv_expanded_image,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(ObjectAnimator.ofFloat(iv_expanded_image, View.Y, startBounds.top, finalBounds.top))
                with(ObjectAnimator.ofFloat(iv_expanded_image, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(iv_expanded_image, View.SCALE_Y, startScale, 1f))
            }
            duration = mShortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    mCurrentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    mCurrentAnimator = null
                }
            })
            start()
        }

        Blurry.with(context)
            .radius(10)
            .sampling(8)
            .color(Color.argb(66, 255, 255, 255))
            .async()
            .animate(100)
            .onto(mFLBlur as ViewGroup?)

        iv_expanded_image.setOnClickListener {
            mCurrentAnimator?.cancel()

            mCurrentAnimator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(iv_expanded_image, View.X, startBounds.left)).apply {
                    with(ObjectAnimator.ofFloat(iv_expanded_image, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(iv_expanded_image, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(iv_expanded_image, View.SCALE_Y, startScale))
                }
                duration = mShortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        thumbView.alpha = 1f
                        iv_expanded_image.visibility = View.GONE
                        mCurrentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        thumbView.alpha = 1f
                        iv_expanded_image.visibility = View.GONE
                        mCurrentAnimator = null
                    }
                })
                start()
            }

            Blurry.delete(mFLBlur)

        }

    }
}

package davydov.annamoney.ui.lottie

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import davydov.annamoney.R
import davydov.annamoney.util.FragmentCompanion

class LottieFragment : Fragment() {

    private lateinit var animationView: LottieAnimationView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.lottie_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animationView = view.findViewById(R.id.lottie_animation_view)
        animationView.repeatCount = LottieDrawable.INFINITE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat(PROGRESS, animationView.progress)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getFloat(PROGRESS)?.let {
            animationView.progress = it
        }

    }

    override fun onResume() {
        super.onResume()
        animationView.playAnimation()
    }

    override fun onPause() {
        super.onPause()
        animationView.pauseAnimation()
    }

    companion object : FragmentCompanion() {
        const val PROGRESS = "animation_progress"
    }
}
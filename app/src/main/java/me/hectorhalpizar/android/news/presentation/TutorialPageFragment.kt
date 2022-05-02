package me.hectorhalpizar.android.news.presentation

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.page_tutorial.view.*
import me.hectorhalpizar.android.news.R
import me.hectorhalpizar.android.news.TutorialActivity
import me.hectorhalpizar.android.news.model.TutorialInfo


class TutorialPageFragment : Fragment() {

    private var animation: Animation? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.page_tutorial, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val pageTutorial = it.getInt(TUTORIAL_PAGE)
            activity?.let { fragmentActivity ->
                (fragmentActivity.findViewById(R.id.stub_import) as ViewStub).let { tutorialPage  ->
                    tutorialPage.layoutResource = pageTutorial
                    tutorialPage.inflate()
                }

                view.tutorial_info.setText(it.getInt(TUTORIAL_INFO))

                val isLastPage = it.getBoolean(TUTORIAL_LAST_PAGE, false)
                if (isLastPage) {
                    view.left_button.visibility = View.GONE
                    view.right_button.setText(R.string.go_to_app)
                } else {

                    if (TutorialActivity.ACTION_MAIN != activity?.intent?.action) {
                        view.left_button.setText(R.string.exit)
                    }

                    view.left_button.visibility = View.VISIBLE

                    view.right_button.setText(R.string.next)
                    view.left_button.setOnClickListener {
                        (fragmentActivity as TutorialActivity).onLeftButtonPress()
                    }
                }

                view.right_button.setOnClickListener {
                    (fragmentActivity as TutorialActivity).onRightButtonPress()
                }

                val tutorialContent = fragmentActivity.getString(it.getInt(TUTORIAL_INFO))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.tutorial_info.text = Html.fromHtml(tutorialContent, Html.FROM_HTML_MODE_LEGACY, ImageGetter(), null)
                } else {
                    view.tutorial_info.text = Html.fromHtml(tutorialContent, ImageGetter(), null)
                }

                animation = AnimationUtils.loadAnimation(activity, R.anim.zoom)
            }
        } ?: throw IllegalStateException("A valid page must be provided")
    }

    override fun onResume() {
        super.onResume()

        activity?.let { a ->
            arguments?.let {
                val pageArrow = it.getInt(TUTORIAL_ARROW)
                val image: ImageView = a.findViewById(pageArrow)
                animation?.let { anim ->
                    image.startAnimation(anim)
                    anim.startOffset = 1000
                    anim.startNow()
                    Log.d("NEWS", "Start anim")
                }
            } ?: run {
                throw IllegalStateException("A valid arrow id must be provided")
            }
        }
    }

    companion object {
        private const val TUTORIAL_PAGE = "tutorial_page"
        private const val TUTORIAL_INFO = "tutorial_info"
        private const val TUTORIAL_LAST_PAGE = "tutorial_last_page"
        private const val TUTORIAL_ARROW = "tutorial_arrow"

        fun newInstance(tutorialInfo: TutorialInfo) : TutorialPageFragment =
            TutorialPageFragment().apply {
                arguments = Bundle().apply {
                    putInt(TUTORIAL_PAGE, tutorialInfo.page)
                    putInt(TUTORIAL_INFO, tutorialInfo.informationStringId)
                    putInt(TUTORIAL_ARROW, tutorialInfo.arrowId)
                    putBoolean(TUTORIAL_LAST_PAGE, tutorialInfo.lastPage)
                }
            }
        }

    interface TutorialPageListener {
        fun onLeftButtonPress()
        fun onRightButtonPress()
    }

    private inner class ImageGetter : Html.ImageGetter {
        override fun getDrawable(source: String?): Drawable? {
            activity?.let { fragmentActivity ->
                source?.let { image ->
                    val drawable = when (image) {
                        "menu.jpg" -> {
                            fragmentActivity.resources?.getDrawable(R.drawable.ic_menu, fragmentActivity.theme)
                        }
                        "refresh.jpg" -> {
                            fragmentActivity.resources?.getDrawable(R.drawable.ic_refresh_tutorial, fragmentActivity.theme)
                        }
                        else -> {
                            null
                        }
                    }
                    drawable?.setBounds(0, 0, 50, 50)
                    return drawable
                }
            } ?: run {
                return null
            }
        }
    }
}
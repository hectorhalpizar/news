package me.hectorhalpizar.android.news.presentation

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_about.view.*
import me.hectorhalpizar.android.news.R
import me.hectorhalpizar.android.news.framework.versionCode
import me.hectorhalpizar.android.news.framework.versionName

class AboutFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false).also { view ->
            activity?.let { fragmentActivity ->
                val appPackageInfo = fragmentActivity.packageManager.getPackageInfo(fragmentActivity.packageName, 0)

                val versionCode = versionCode(appPackageInfo)
                val versionName = versionName(appPackageInfo)
                val aboutContent = String.format(
                    fragmentActivity.getString(R.string.news_about),
                    versionName,
                    versionCode
                )

                view.about_content.movementMethod = LinkMovementMethod.getInstance()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.about_content.text = Html.fromHtml(aboutContent, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    view.about_content.text = Html.fromHtml(aboutContent)
                }
            }
        }
    }
}
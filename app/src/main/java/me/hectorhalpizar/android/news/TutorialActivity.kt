package me.hectorhalpizar.android.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import me.hectorhalpizar.android.news.model.TutorialInfo
import me.hectorhalpizar.android.news.presentation.TutorialPageFragment

class TutorialActivity :  AppCompatActivity(), TutorialPageFragment.TutorialPageListener {

    private lateinit var mPager: ViewPager
    private val tutorialPages: List<TutorialInfo> =
        listOf(TutorialInfo(R.layout.page_tutorial_main_menu, R.string.tutorial_info_main_menu, arrowId = R.id.arrow_image_1),
               TutorialInfo(R.layout.page_tutorial_sections, R.string.tutorial_info_sections, arrowId = R.id.arrow_image_4),
               TutorialInfo(R.layout.page_tutorial_refresh, R.string.tutorial_info_refresh, arrowId = R.id.arrow_image_3),
               TutorialInfo(R.layout.page_tutorial_read_more, R.string.tutorial_info_more, true, R.id.arrow_image_2))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isTutorialFinish()) {
            goToTheApp()
            return
        }

        setContentView(R.layout.activity_tutorial)

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.pager)
        val tabLayout = findViewById<View>(R.id.tabDots) as TabLayout
        tabLayout.setupWithViewPager(mPager, true)


        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = TutorialSlidePagerAdapter(supportFragmentManager)
        mPager.adapter = pagerAdapter
    }

    override fun onBackPressed() {
        if (mPager.currentItem == 0) {
            Toast.makeText(this, "You must finish the tutorial in order to get to the app", Toast.LENGTH_SHORT).show()
        } else {
            mPager.currentItem = mPager.currentItem - 1
        }
    }

    override fun onLeftButtonPress() {
        finishTutorial()
    }

    override fun onRightButtonPress() {
        if (mPager.currentItem >= tutorialPages.size - 1) {
            finishTutorial()
        } else {
            mPager.currentItem = mPager.currentItem + 1
        }
    }

    private fun goToTheApp() {
        Intent(this, NewsActivity::class.java).also { intent ->
            startActivity(intent)
            finish()
        }
    }

    private fun finishTutorial() {
        setTutorialAsFinish()
        goToTheApp()
    }

    private fun isTutorialFinish(): Boolean {
        val sharedPreferences = this.getSharedPreferences(TUTORIAL_PREFERENCE, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(TUTORIAL_IS_FINISH, false)
    }

    private fun setTutorialAsFinish() {
        val sharedPreferences = this.getSharedPreferences(TUTORIAL_PREFERENCE, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(TUTORIAL_IS_FINISH, true).apply()
    }

    private inner class TutorialSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = tutorialPages.size
        override fun getItem(position: Int): Fragment = TutorialPageFragment.newInstance(tutorialPages[position])
    }

    companion object {
        private const val TUTORIAL_PREFERENCE = "tutorial_reference"
        private const val TUTORIAL_IS_FINISH = "tutorial_is_finish"
    }
}
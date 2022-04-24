package me.hectorhalpizar.android.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.hectorhalpizar.android.news.presentation.TopStoriesFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.top_stories, TopStoriesFragment.newInstance("ARTS"))
            .commit()
    }
}
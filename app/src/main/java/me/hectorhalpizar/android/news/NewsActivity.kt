package me.hectorhalpizar.android.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import me.hectorhalpizar.android.news.databinding.ActivityNewsBinding
import me.hectorhalpizar.android.news.framework.versionCode
import me.hectorhalpizar.android.news.framework.versionName
import me.hectorhalpizar.core.news.domain.Section


class NewsActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNews.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_news)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(navigationSection.keys.toSet(), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_about -> {
                    Intent(this, AboutActivity::class.java).also { intent ->
                        startActivity(intent)
                    }
                }
                R.id.nav_provided_by -> {
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.nytimes.com/")).also { intent ->
                        startActivity(intent)
                    }
                }
                else -> {
                    navController.navigate(it.itemId)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.news, menu)
        setHeaderVersion()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_news)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setHeaderVersion() {
        val appPackageInfo = packageManager.getPackageInfo(packageName, 0)

        val versionCode = versionCode(appPackageInfo)
        val versionName = versionName(appPackageInfo)

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView: View = navigationView.getHeaderView(0)
        val version : TextView = headerView.findViewById(R.id.version_header)
        version.text = String.format(getString(R.string.version), versionName, versionCode)
    }

    private val navigationSection: HashMap<Int, Section> = hashMapOf(
        R.id.nav_arts to Section.ARTS,
        R.id.nav_automobiles to Section.AUTOMOBILES,
        R.id.nav_books to Section.BOOKS,
        R.id.nav_business to Section.BUSINESS,
        R.id.nav_fashion to Section.FASHION,
        R.id.nav_food to Section.FOOD,
        R.id.nav_health to Section.HEALTH,
        R.id.nav_home to Section.HOME,
        R.id.nav_insider to Section.INSIDER,
        R.id.nav_magazine to Section.MAGAZINE,
        R.id.nav_movies to Section.MOVIES,
        R.id.nav_obituaries to Section.OBITUARIES,
        R.id.nav_opinion to Section.OPINION,
        R.id.nav_politics to Section.POLITICS,
        R.id.nav_real_estate to Section.REAL_ESTATE,
        R.id.nav_science to Section.SCIENCE,
        R.id.nav_sports to Section.SPORTS,
        R.id.nav_sunday_review to Section.SUNDAY_REVIEW,
        R.id.nav_technology to Section.TECHNOLOGY,
        R.id.nav_theater to Section.THEATER,
        R.id.nav_t_magazine to Section.T_MAGAZINE,
        R.id.nav_travel to Section.TRAVEL,
        R.id.nav_upshot to Section.UPSHOT,
        R.id.nav_us to Section.US,
        R.id.nav_world to Section.WORLD
    )
}
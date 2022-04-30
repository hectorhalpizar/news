package me.hectorhalpizar.android.news.presentation

import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import me.hectorhalpizar.android.news.R

abstract class BaseFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        handleRefreshMenuButton()
    }

    private fun handleRefreshMenuButton() {
        activity?.let {
            val toolbar: Toolbar = it.findViewById(R.id.toolbar)
            val menu: Menu = toolbar.menu
            menu.findItem(R.id.menu_refresh)?.let { menuItem ->
                menuItem.isVisible = this !is AboutFragment
            }
        }
    }
}
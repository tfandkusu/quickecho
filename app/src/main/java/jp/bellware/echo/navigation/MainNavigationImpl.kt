package jp.bellware.echo.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import jp.bellware.echo.view.pager.ViewPagerHostFragmentDirections

class MainNavigationImpl : MainNavigation {
    override fun callSettingFragment(fragment: Fragment) {
        val action = ViewPagerHostFragmentDirections.actionViewPagerHostFragmentToSettingFragment(null)
        fragment.findNavController().navigate(action)
    }
}

package jp.bellware.echo.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import jp.bellware.echo.view.main.MainFragmentDirections

class MainNavigationImpl : MainNavigation {
    override fun callSettingFragment(fragment: Fragment) {
        val action = MainFragmentDirections.actionMainFragmentToSettingFragment(null)
        fragment.findNavController().navigate(action)
    }
}
package jp.bellware.echo.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.bellware.echo.view.setting.AboutFragment
import jp.bellware.echo.view.setting.SettingFragmentArgs
import jp.bellware.echo.view.setting.SettingFragmentDirections

class SettingNavigationImpl : SettingNavigation {
    override fun navigateToLink(fragment: Fragment) {
        val safeArgs: SettingFragmentArgs by fragment.navArgs()
        if (safeArgs.type == AboutFragment.LINK_TYPE) {
            val action = SettingFragmentDirections.actionSettingFragmentToAboutFragmentWithNoAnimation()
            fragment.findNavController().navigate(action)
        }
    }

    override fun navigateToAbout(fragment: Fragment) {
        val action = SettingFragmentDirections.actionSettingFragmentToAboutFragment()
        fragment.findNavController().navigate(action)
    }
}

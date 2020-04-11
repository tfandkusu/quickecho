package jp.bellware.echo.navigation

import androidx.fragment.app.Fragment

interface MainNavigation {
    /**
     * 設定画面に遷移する
     */
    fun callSettingFragment(fragment: Fragment)
}
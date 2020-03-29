package jp.bellware.echo.navigation

import androidx.fragment.app.Fragment

/**
 * 設定画面の画面遷移担当
 */
interface SettingNavigation {
    /**
     * ディープリンク処理を呼び出す
     */
    fun navigateToLink(fragment: Fragment)

    /**
     * アプリについて画面に遷移する
     */
    fun navigateToAbout(fragment: Fragment)
}

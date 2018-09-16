package jp.bellware.echo.setting

import android.databinding.ObservableField
import android.content.Context
import android.content.pm.PackageManager
import jp.bellware.echo.R

/**
 * アプリについて画面のビューモデル
 */
class AboutViewModel(private val context : Context){
    /**
     * バージョン文字列
     */
    val versionText = ObservableField<String>()

    fun onCreate(){
        //バージョン名を設定
        try {
            val pm = context.packageManager
            val packageInfo = pm.getPackageInfo(context.packageName, 0)
            versionText.set("${context.getString(R.string.version)} ${packageInfo.versionName}")
        } catch (e: PackageManager.NameNotFoundException) {
            //おこらない
        }

    }

}
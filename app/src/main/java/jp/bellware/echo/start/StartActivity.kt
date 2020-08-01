package jp.bellware.echo.start

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import jp.bellware.echo.R
import jp.bellware.echo.util.EchoFirebaseDynamicLinksUtil
import jp.bellware.echo.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_start.*

/**
 * 開始アクティビティ。
 */
class StartActivity : AppCompatActivity() {
    companion object {
        private const val CODE_PERMISSION = 1

        private const val CODE_MAIN = 2

        /**
         * 通知のチャンネルID
         */
        const val CHANNEL_ID = "main"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        //ツールバー設定
        this.setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            // 起動時のみ
            checkPermission()
            // 通知チャンネルの登録
            createNotificationChannel()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_MAIN) {
            finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (CODE_PERMISSION == requestCode) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                callMainActivityWithLinks()
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                finish()
            }
            return
        }
    }

    /**
     * 実行時パーミッションをチェックする
     */
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                //拒否した場合は説明を表示
                showInformation()
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                //許可を求める
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        CODE_PERMISSION)
            }
        } else {
            //すでに許可されている
            callMainActivityWithLinks()
        }
    }

    /**
     * メイン画面をFirebase Dynamic Linksのパラメータを持って呼び出す
     */
    private fun callMainActivityWithLinks() {
        EchoFirebaseDynamicLinksUtil.process(this, intent) { type ->
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            type?.let {
                intent.putExtra(MainActivity.EXTRA_TYPE, it)
            }
            startActivityForResult(intent, CODE_MAIN)
        }
    }

    /**
     * 実行時パーミッションの解説
     */
    private fun showInformation() {
        val adb = AlertDialog.Builder(this)
        adb.setTitle(R.string.title_permission)
        adb.setMessage(R.string.message_permission)
        adb.setCancelable(false)
        adb.setPositiveButton(R.string.ok) { _, _ ->
            callApplicationDetailActivity()
            finish()
        }.setNegativeButton(R.string.cancel) { _, _ -> finish() }
        adb.show()
    }

    /**
     * アプリ詳細を呼び出す
     */
    private fun callApplicationDetailActivity() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:$packageName"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }


    /**
     * 通知チャンネルを登録する
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}

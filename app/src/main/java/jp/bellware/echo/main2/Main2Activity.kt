package jp.bellware.echo.main2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import jp.bellware.echo.R
import jp.bellware.echo.actioncreator.MainActionCreator
import jp.bellware.echo.setting.SettingActivity
import jp.bellware.echo.store.MainStore
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.main_control2.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class Main2Activity : AppCompatActivity() {

    companion object {
        private const val CODE_SETTING = 1
    }

    private val store: MainStore by viewModel()

    private val soundEffect: SoundEffectViewModel by viewModel()

    private val actionCreator: MainActionCreator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)
        // 効果音読み込み
        soundEffect.onCreate(this) {
            actionCreator.onSoundLoaded()
        }
        // 録音ボタンが押された
        store.recordClickable.observe(this, Observer {
            if (it == true) {
                record.setOnClickListener {
                    soundEffect.start()
                    actionCreator.onRecordClick()
                }
            }
        })

        // StoreとViewを繋げる
        store.record.observe(this, Observer { flag ->
            flag?.let {
                if (it)
                    record.show()
                else
                    record.hide()
            }
        })
        store.play.observe(this, Observer { flag ->
            flag?.let {
                if (it)
                    play.show()
                else
                    play.hide()
            }
        })
        store.stop.observe(this, Observer { flag ->
            flag?.let {
                if (it)
                    stop.show()
                else
                    stop.hide()
            }
        })
        store.replay.observe(this, Observer { flag ->
            flag?.let {
                if (it)
                    replay.show()
                else
                    replay.hide()
            }
        })
        store.delete.observe(this, Observer { flag ->
            flag?.let {
                if (it)
                    delete.show()
                else
                    delete.hide()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.setting) {
            callSettingActivity()
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_SETTING) {
            soundEffect.onSettingUpdate()
        }
    }

    private fun callSettingActivity() {
        val intent = Intent(this, SettingActivity::class.java)
        startActivityForResult(intent, CODE_SETTING)
    }
}

package jp.bellware.echo.view.memo

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.bellware.echo.actioncreator.memo.SoundMemoActionCreator
import jp.bellware.echo.memo.R
import jp.bellware.echo.store.memo.SoundMemoStore
import jp.bellware.echo.view.setting.SettingActivityAlias
import kotlinx.android.synthetic.main.activity_sound_memo.*


/**
 * 音声メモ画面。
 */
@AndroidEntryPoint
class SoundMemoActivity : AppCompatActivity() {

    private val actionCreator: SoundMemoActionCreator by viewModels()

    private val store: SoundMemoStore by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_memo)
        setSupportActionBar(toolbar)
        volumeControlStream = AudioManager.STREAM_MUSIC
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setUpRecyclerView()
        actionCreator.onCreate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.setting -> {
                callSettingActivity()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun callSettingActivity() {
        val intent = Intent(this, SettingActivityAlias::class.java)
        startActivity(intent)
    }

    /**
     * 一覧表示の設定
     */
    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = GroupAdapter<GroupieViewHolder>()
        recyclerView.adapter = adapter
        store.items.observe(this) { items ->
            adapter.update(items.map {
                SoundMemoGroupieItem(it)
            })
        }
    }
}

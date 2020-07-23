package jp.bellware.echo.view.pager

import android.media.AudioManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import jp.bellware.echo.main.R
import kotlinx.android.synthetic.main.activity_pager.*

/**
 * 横スワイプで録音画面と音声メモ画面を切り替える画面
 */
class PagerActivity : AppCompatActivity() {
    companion object {
        /**
         * Firebase Dynamic Linksから送られてきた画面タイプ
         */
        const val EXTRA_TYPE = "type"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)
        setSupportActionBar(toolbar)

        viewPager.adapter = QuickEchoFragmentStateAdapter(this)
        viewPager.offscreenPageLimit = 1
        val childView = viewPager.getChildAt(0)
        if (childView is RecyclerView) {
            childView.setOverScrollMode(View.OVER_SCROLL_NEVER)
        }

        //ボリューム調整を音楽にする
        volumeControlStream = AudioManager.STREAM_MUSIC
    }
}
package jp.bellware.echo.view.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.flutter.embedding.android.FlutterFragment
import jp.bellware.echo.view.main.MainFragment

/**
 * ページ0は録音ページ、ページ1は音声メモ画面(作成予定)
 */
class QuickEchoFragmentStateAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            MainFragment()
        else
            FlutterFragment.createDefault()
    }
}

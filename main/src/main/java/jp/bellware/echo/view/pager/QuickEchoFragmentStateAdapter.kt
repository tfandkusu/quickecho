package jp.bellware.echo.view.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import io.flutter.embedding.android.FlutterFragment
import jp.bellware.echo.view.main.MainFragment

/**
 * ページ0は録音ページ、ページ1は音声メモ画面(作成予定)
 */
class QuickEchoFragmentStateAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = 2

    override fun getItem(position: Int): Fragment {
        return if (position == 0)
            MainFragment()
        else
            FlutterFragment.withNewEngine()
                    .shouldAttachEngineToActivity(false)
                    .build()
    }
//    override fun getItemCount() = 2
//
//    override fun createFragment(position: Int): Fragment {
//        return if (position == 0)
//            MainFragment()
//        else
//            EmptyFragment()
////        else {
////            val st = System.currentTimeMillis()
////            // Flutterフラグメント
////            val f = FlutterFragment.withNewEngine()
////                    .shouldAttachEngineToActivity(false)
////                    .build() as Fragment
////            Timber.d("FlutterFragment = " + (System.currentTimeMillis() - st))
////            return f
////        }
}

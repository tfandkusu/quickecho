package jp.bellware.echo.view.pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import jp.bellware.echo.view.main.MainFragment
import org.koin.core.KoinComponent
import org.koin.core.inject

class QuickEchoFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment), KoinComponent {

    private val factory: FlutterFragmentFactory by inject()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            MainFragment()
        } else {
            factory.create()
        }
    }
}


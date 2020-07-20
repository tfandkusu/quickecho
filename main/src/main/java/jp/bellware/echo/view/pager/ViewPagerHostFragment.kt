package jp.bellware.echo.view.pager

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import jp.bellware.echo.main.R
import jp.bellware.echo.navigation.MainNavigation
import kotlinx.android.synthetic.main.fragment_view_pager_host.*
import org.koin.android.ext.android.inject

class ViewPagerHostFragment : Fragment() {

    /**
     * 画面遷移担当
     */
    private val navigation: MainNavigation by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_view_pager_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager.adapter = QuickEchoFragmentStateAdapter(this)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.setting) {
            navigation.callSettingFragment(this)
            return true
        }
        return false
    }
}

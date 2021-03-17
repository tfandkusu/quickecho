package jp.bellware.echo.view.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import jp.bellware.echo.setting.R
import kotlinx.android.synthetic.main.fragment_open_source_license_2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ライブラリではなく、AACファイルのエンコードとデコードでソースコードを使わせて頂いたところのライセンス表記を担当。
 */
class OpenSourceLicense2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_open_source_license_2, container, false)
    }

    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        setUpLicense()
    }

    private fun setUpLicense() = GlobalScope.launch(Dispatchers.Main) {
        // Apacheライセンスのみ
        val text = withContext(Dispatchers.IO) {
            val fis = requireContext().assets.open("apache_license.txt")
            val data = fis.readBytes()
            String(data)
        }
        val items = resources.getStringArray(R.array.oss_titles).map {
            OpenSourceLicenseGroupieItem(it, text)
        }
        adapter.update(items)
    }
}

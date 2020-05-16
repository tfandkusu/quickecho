package jp.bellware.echo.view.setting

import android.content.Intent
import android.net.Uri
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import jp.bellware.echo.setting.R
import kotlinx.android.synthetic.main.list_item_open_source_license.view.*

class OpenSourceLicenseGroupieItem(val name: String, private val body: String) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val context = viewHolder.itemView.context
        viewHolder.apply {
            itemView.title.text = name
            val url = "%s%s".format(context.getText(R.string.github_url), name)
            itemView.url.text = url
            itemView.body.text = body
            itemView.link.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        }
    }

    override fun getLayout() = R.layout.list_item_open_source_license

    override fun isSameAs(other: com.xwray.groupie.Item<*>): Boolean {
        if (other is OpenSourceLicenseGroupieItem) {
            return this.name == other.name
        }
        return false
    }
}

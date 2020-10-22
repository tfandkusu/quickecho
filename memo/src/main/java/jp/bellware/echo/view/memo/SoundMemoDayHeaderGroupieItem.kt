package jp.bellware.echo.view.memo

import android.text.Html
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import jp.bellware.echo.action.memo.SoundMemoDayHeader
import jp.bellware.echo.memo.R
import jp.bellware.echo.util.memo.DayHeaderTextUtil
import kotlinx.android.synthetic.main.list_item_sound_memo_day_header.view.*
import java.util.*

class SoundMemoDayHeaderGroupieItem(val dayHeader: SoundMemoDayHeader) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val context = viewHolder.itemView.context
        dayHeader.ymd.apply {
            viewHolder.itemView.tag = "header %d %d %d".format(year, month, day)
        }
        viewHolder.itemView.apply {
            var text: String
            if (dayHeader.today) {
                text = context.getString(R.string.today)
            } else {
                text = if (dayHeader.thisYear) {
                    DayHeaderTextUtil.thisYear(dayHeader.ymd, Locale.getDefault())
                } else {
                    DayHeaderTextUtil.withYear(dayHeader.ymd, Locale.getDefault())
                }
                // 日本語の時は土曜日を青、日曜日を赤にする
                if (Locale.getDefault() == Locale.JAPAN || Locale.getDefault() == Locale.JAPANESE) {
                    text = text.replace("（土）", "（<font color=\"#2196f3\">土</font>）")
                    text = text.replace("（日）", "（<font color=\"#f44336\">日</font>）")
                }
            }
            day.text = Html.fromHtml(text, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
        }
    }

    override fun getLayout() = R.layout.list_item_sound_memo_day_header

    override fun isSameAs(other: com.xwray.groupie.Item<*>): Boolean {
        return other is SoundMemoDayHeaderGroupieItem && other.dayHeader.ymd == dayHeader.ymd
    }

    override fun hasSameContentAs(other: com.xwray.groupie.Item<*>): Boolean {
        return other is SoundMemoDayHeaderGroupieItem && other.dayHeader == dayHeader
    }
}

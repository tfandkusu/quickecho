package jp.bellware.echo.view.memo

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import jp.bellware.echo.memo.R

class SoundMemoDayHeaderGroupieItem(val today: Boolean, val createAt: Long) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

    override fun getLayout() = R.layout.list_item_sound_memo_day_header
}

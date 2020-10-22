package jp.bellware.echo.view.memo

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.doist.recyclerviewext.sticky_headers.StickyHeaders

class StickyHeaderGroupAdapter : GroupAdapter<GroupieViewHolder>(), StickyHeaders {
    override fun isStickyHeader(position: Int): Boolean {
        return getItem(position) is SoundMemoDayHeaderGroupieItem
    }

}

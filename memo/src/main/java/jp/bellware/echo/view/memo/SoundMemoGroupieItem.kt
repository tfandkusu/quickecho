package jp.bellware.echo.view.memo

import androidx.core.view.isVisible
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import jp.bellware.echo.memo.R
import jp.bellware.echo.repository.data.SoundMemo
import kotlinx.android.synthetic.main.list_item_sound_memo.view.*
import java.text.SimpleDateFormat
import java.util.*

class SoundMemoGroupieItem(val soundMemo: SoundMemo,
                           val playing: Boolean = false,
                           val visualVolume: Float = 0f) : Item(soundMemo.id) {

    private val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            number.text = soundMemo.id.toString()
            time.text = sdf.format(Date(soundMemo.createdAt))
            if (soundMemo.temporal) {
                // 一時保存
                temporal.isVisible = true
                tapToSave.isVisible = true
            } else {
                // 永続保存
                temporal.isVisible = false
                tapToSave.isVisible = false
            }

        }
    }

    override fun getLayout() = R.layout.list_item_sound_memo

    override fun hasSameContentAs(other: com.xwray.groupie.Item<*>): Boolean {
        // 同じ情報は改めて再描画しないようにする。
        return if (other is SoundMemoGroupieItem) {
            other.soundMemo == soundMemo && other.playing == playing && other.visualVolume == visualVolume
        } else {
            false
        }
    }
}

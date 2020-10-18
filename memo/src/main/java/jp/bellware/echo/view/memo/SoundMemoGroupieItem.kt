package jp.bellware.echo.view.memo

import androidx.core.view.isVisible
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import jp.bellware.echo.memo.R
import jp.bellware.echo.repository.data.SoundMemo
import kotlinx.android.synthetic.main.list_item_sound_memo.view.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class SoundMemoGroupieItem(private val soundMemo: SoundMemo,
                           private val playing: Boolean = false,
                           private val visualVolume: Float = 0f) : Item(soundMemo.id) {

    private val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Timber.d("bind $position")
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

}

package jp.bellware.echo.datastore.local

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.bellware.echo.datastore.local.schema.LocalSoundMemo

@Database(entities = [LocalSoundMemo::class], version = 1)
abstract class QuickEchoDatabase : RoomDatabase() {
    abstract fun soundMemoDao(): SoundMemoDao
}

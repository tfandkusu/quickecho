package jp.bellware.echo.datastore.local

import android.content.Context
import androidx.room.Room

object QuickEchoDatabaseFactory {
    fun create(context: Context): QuickEchoDatabase {
        return Room.databaseBuilder(
            context,
            QuickEchoDatabase::class.java, "database.sqlite3"
        ).build()
    }
}

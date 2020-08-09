package jp.bellware.echo.mapper

import jp.bellware.echo.datastore.local.schema.LocalSoundMemo
import jp.bellware.echo.repository.data.SoundMemo

/**
 * 音声メモのデータ変換
 */
object SoundMemoMapper {
    /**
     * Repository層 → LocalDataStore層の変換
     */
    fun map(soundMemo: SoundMemo): LocalSoundMemo {
        return LocalSoundMemo(soundMemo.id,
                soundMemo.temporal,
                soundMemo.createdAt,
                soundMemo.fileName,
                soundMemo.locationStatus,
                soundMemo.longitude,
                soundMemo.latitude,
                soundMemo.prefecture,
                soundMemo.city,
                soundMemo.street,
                soundMemo.textStatus,
                soundMemo.text)
    }
}

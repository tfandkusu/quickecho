package jp.bellware.echo.datastore.local.schema

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * 音声メモ
 * @param id 連番、プライマリーキー
 * @param temporal 一時保存フラグ
 * @param createdAt 作成日(1970年からのミリ秒)
 * @param fileName ファイル名
 * @param locationStatus 位置情報取得ステータス
 * @param longitude 経度
 * @param latitude 緯度
 * @param prefecture 都道府県
 * @param city 市区町村
 * @param street 町名以下
 * @param textStatus 認識結果取得ステータス
 * @param text 認識結果
 */
@Entity
data class LocalSoundMemo(@PrimaryKey(autoGenerate = true) val id: Long,
                          val temporal: Boolean,
                          val createdAt: Long,
                          val fileName: String,
                          val locationStatus: Int,
                          val longitude: Double,
                          val latitude: Double,
                          val prefecture: String,
                          val city: String,
                          val street: String,
                          val textStatus: Int,
                          val text: String) {
    companion object {
        /**
         * 位置情報の機能は未実装
         */
        const val LOCATION_STATUS_NOT_IMPLEMENTED = 1

        /**
         * 認識結果の機能は未実装
         */
        const val TEXT_STATUS_NOT_IMPLEMENTED = 1
    }
}


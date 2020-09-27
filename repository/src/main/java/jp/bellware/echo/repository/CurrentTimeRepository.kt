package jp.bellware.echo.repository

import javax.inject.Inject

/**
 * 現在時刻提供担当。単体テストのために現在時刻をモック化出来るようにする。
 */
interface CurrentTimeRepository {
    fun now(): Long
}

class CurrentTimeRepositoryImpl @Inject constructor() : CurrentTimeRepository {
    override fun now(): Long {
        return System.currentTimeMillis()
    }
}

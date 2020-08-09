package jp.bellware.echo.actioncreator

import javax.inject.Inject

/**
 * 待機担当。単体テストのためにインターフェースを定義
 */
interface DelayActionCreatorHelper {
    /**
     * 指定したミリ秒待機する
     * @param ms 待機時間
     */
    suspend fun delay(ms: Int)
}

class DelayActionCreatorHelperImpl @Inject constructor() : DelayActionCreatorHelper {
    override suspend fun delay(ms: Int) {
        kotlinx.coroutines.delay(ms.toLong())
    }
}

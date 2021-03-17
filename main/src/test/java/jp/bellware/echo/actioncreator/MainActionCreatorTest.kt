package jp.bellware.echo.actioncreator

import io.mockk.MockKAnnotations
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import jp.bellware.echo.action.MainDeleteAction
import jp.bellware.echo.action.MainMaxRecordTimeOverAction
import jp.bellware.echo.action.MainMuteAction
import jp.bellware.echo.action.MainNoRecordAction
import jp.bellware.echo.action.MainPlayAction
import jp.bellware.echo.action.MainPrePlayAction
import jp.bellware.echo.action.MainPreRecordAction
import jp.bellware.echo.action.MainReadyAction
import jp.bellware.echo.action.MainRecordAction
import jp.bellware.echo.action.MainReplayAction
import jp.bellware.echo.action.MainRestoreEndAction
import jp.bellware.echo.action.MainRestoreStartAction
import jp.bellware.echo.action.MainSoundMemoAction
import jp.bellware.echo.action.MainSoundMemoButtonVisibilityAction
import jp.bellware.echo.action.MainStopAction
import jp.bellware.echo.repository.SettingRepository
import jp.bellware.echo.repository.SoundRepository
import jp.bellware.echo.util.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class MainActionCreatorTest {

    @MockK(relaxed = true)
    lateinit var dispatcher: Dispatcher

    @MockK(relaxed = true)
    lateinit var delayActionCreatorHelper: DelayActionCreatorHelper

    @MockK(relaxed = true)
    lateinit var settingRepository: SettingRepository

    @MockK(relaxed = true)
    lateinit var soundRepository: SoundRepository

    /**
     * テスト対象
     */
    private lateinit var actionCreator: MainActionCreator

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        // スレッドを切り替えない
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockKAnnotations.init(this)
        actionCreator = MainActionCreator(
            dispatcher, delayActionCreatorHelper,
            settingRepository, soundRepository
        )
    }

    @Test
    fun onCreate() = runBlocking {
        every {
            settingRepository.isShowSoundMemoButton()
        } returns flow {
            emit(true)
        }
        actionCreator.onCreate().join()
        coVerifySequence {
            dispatcher.dispatch(MainSoundMemoButtonVisibilityAction(true))
        }
    }

    @Test
    fun onSoundLoadedFirstTime() = runBlocking {
        actionCreator.onSoundLoaded(false).join()
        verifySequence {
            dispatcher.dispatch(MainReadyAction)
        }
    }

    /**
     * 再生または停止状態でプロセスキルして、そこからの復帰ケース
     */
    @Test
    fun onSoundLoadedRestore() = runBlocking {
        actionCreator.onSoundLoaded(true).join()
        coVerifySequence {
            dispatcher.dispatch(MainReadyAction)
            dispatcher.dispatch(MainRestoreStartAction)
            soundRepository.restore()
            dispatcher.dispatch(MainStopAction)
            dispatcher.dispatch(MainRestoreEndAction)
        }
    }

    @Test
    fun onRecordClick() = runBlocking {
        actionCreator.onRecordClick().join()
        coVerifySequence {
            dispatcher.dispatch(MainPreRecordAction)
            delayActionCreatorHelper.delay(500)
            dispatcher.dispatch(MainRecordAction)
        }
    }

    @Test
    fun onDeleteClick() = runBlocking {
        actionCreator.onDeleteClick().join()
        coVerifySequence {
            dispatcher.dispatch(MainDeleteAction)
            delayActionCreatorHelper.delay(200)
            dispatcher.dispatch(MainReadyAction)
        }
    }

    @Test
    fun onReplayClick() {
        actionCreator.onReplayClick()
        verifySequence {
            dispatcher.dispatch(MainReplayAction)
        }
    }

    @Test
    fun onPlayClickIncludeSound() = runBlocking {
        actionCreator.onPlayClick(true).join()
        coVerifySequence {
            dispatcher.dispatch(MainPrePlayAction)
            delayActionCreatorHelper.delay(550)
            dispatcher.dispatch(MainPlayAction)
        }
    }

    @Test
    fun onPlayClickNoSound() = runBlocking {
        actionCreator.onPlayClick(false).join()
        coVerifySequence {
            dispatcher.dispatch(MainNoRecordAction)
            dispatcher.dispatch(MainDeleteAction)
            delayActionCreatorHelper.delay(200)
            dispatcher.dispatch(MainReadyAction)
        }
    }

    @Test
    fun onStopClick() {
        actionCreator.onStopClick()
        verifySequence {
            dispatcher.dispatch(MainStopAction)
        }
    }

    @Test
    fun onMute() {
        actionCreator.onMute()
        verifySequence {
            dispatcher.dispatch(MainMuteAction)
        }
    }

    @Test
    fun onMaxRecordTimeOverIncludeSound() {
        actionCreator.onMaxRecordTimeOver(true)
        verifySequence {
            dispatcher.dispatch(MainMaxRecordTimeOverAction(true))
        }
    }

    @Test
    fun onMaxRecordTimeOverNoSound() {
        actionCreator.onMaxRecordTimeOver(false)
        verifySequence {
            dispatcher.dispatch(MainMaxRecordTimeOverAction(false))
        }
    }

    @Test
    fun soundMemo() {
        actionCreator.onSoundMemoClick()
        verifySequence {
            dispatcher.dispatch(MainSoundMemoAction)
        }
    }
}

package jp.bellware.echo.actioncreator

import io.mockk.MockKAnnotations
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import jp.bellware.echo.action.*
import jp.bellware.echo.util.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class MainActionCreatorTest {

    @MockK(relaxed = true)
    lateinit var dispatcher: Dispatcher

    @MockK(relaxed = true)
    lateinit var delayActionCreatorHelper: DelayActionCreatorHelper

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
        actionCreator = MainActionCreator(dispatcher, delayActionCreatorHelper)
    }

    @Test
    fun onSoundLoaded() {
        actionCreator.onSoundLoaded()
        verifySequence {
            dispatcher.dispatch(MainReadyAction)
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
}
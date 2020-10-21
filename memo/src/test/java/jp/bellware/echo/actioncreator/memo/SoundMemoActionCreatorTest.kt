package jp.bellware.echo.actioncreator.memo

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import jp.bellware.echo.action.memo.SoundMemoLastSaveIdAction
import jp.bellware.echo.action.memo.SoundMemoListUpdateAction
import jp.bellware.echo.repository.SoundMemoRepository
import jp.bellware.echo.repository.data.SoundMemo
import jp.bellware.echo.util.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class SoundMemoActionCreatorTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @MockK(relaxed = true)
    private lateinit var dispatcher: Dispatcher

    @MockK(relaxed = true)
    private lateinit var repository: SoundMemoRepository


    private lateinit var actionCreator: SoundMemoActionCreator

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        actionCreator = SoundMemoActionCreator(dispatcher, repository)
    }

    @ExperimentalCoroutinesApi
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun updateList() = runBlocking {
        val items = listOf(SoundMemo(1,
                false,
                1000,
                "output1.aac",
                1,
                139.737056,
                35.677,
                "東京都",
                "港区",
                "赤坂3-1-6",
                2,
                "録音したこと"),
                SoundMemo(2,
                        true,
                        2000,
                        "output2.aac",
                        1,
                        139.623833,
                        35.906278,
                        "埼玉県",
                        "大宮区",
                        "",
                        SoundMemo.TEXT_STATUS_NOT_IMPLEMENTED,
                        ""))
        every {
            repository.index()
        } returns flow {
            emit(items)
        }
        actionCreator.updateList().join()
        coVerifySequence {
            repository.index()
            dispatcher.dispatch(SoundMemoListUpdateAction(items))
        }
    }

    @Test
    fun checkLastSaveId() = runBlocking {
        coEvery {
            repository.getLastId()
        } returns flow {
            emit(1L)
        }
        actionCreator.checkLastSaveId()
        coVerifySequence {
            repository.getLastId()
            dispatcher.dispatch(SoundMemoLastSaveIdAction(1L))
        }
    }
}


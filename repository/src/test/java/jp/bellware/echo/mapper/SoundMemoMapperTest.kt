package jp.bellware.echo.mapper

import io.kotlintest.shouldBe
import jp.bellware.echo.datastore.local.schema.LocalSoundMemo
import jp.bellware.echo.repository.data.SoundMemo
import org.junit.Test

class SoundMemoMapperTest {

    @Test
    fun mapRepository() {
        val st = System.currentTimeMillis()
        val m = SoundMemo(
            1,
            true,
            st,
            "test1.aac",
            1,
            139.0,
            35.0,
            "東京都",
            "港区",
            "赤坂3-1-6",
            2,
            "録音したこと"
        )
        val lm = LocalSoundMemo(
            1,
            true,
            st,
            "test1.aac",
            1,
            139.0,
            35.0,
            "東京都",
            "港区",
            "赤坂3-1-6",
            2,
            "録音したこと"
        )
        lm shouldBe SoundMemoMapper.map(m)
    }

    @Test
    fun mapLocalDataStore() {
        val st = System.currentTimeMillis()
        val lm = LocalSoundMemo(
            1,
            true,
            st,
            "test1.aac",
            1,
            139.0,
            35.0,
            "東京都",
            "港区",
            "赤坂3-1-6",
            2,
            "録音したこと"
        )
        val m = SoundMemo(
            1,
            true,
            st,
            "test1.aac",
            1,
            139.0,
            35.0,
            "東京都",
            "港区",
            "赤坂3-1-6",
            2,
            "録音したこと"
        )

        m shouldBe SoundMemoMapper.map(lm)
    }
}

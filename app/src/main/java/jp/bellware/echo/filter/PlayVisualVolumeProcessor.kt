package jp.bellware.echo.filter

/**
 * 視覚的に分かりやすい音量表示を行う。録音用。
 */
class PlayVisualVolumeProcessor : VisualVolumeProcessor {

    private var vv = 0f

    override fun reset() {
        vv = 0f
    }

    override fun filter(s: Float) {
        var s = s
        if (s < 0)
            s *= -1f
        if (s > vv)
            vv = s
        else {
            //少しづつ後退させる
            vv -= 2f * 1f / 44100
        }
    }

    override fun getVolume(): Float {
        return vv
    }

    override fun isIncludeSound(): Boolean {
        return false
    }
}

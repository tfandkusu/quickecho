package jp.bellware.echo.view.pager

import androidx.fragment.app.Fragment
import io.flutter.embedding.android.FlutterFragment

class FlutterFragmentFactoryImpl : FlutterFragmentFactory {
    override fun create(): Fragment {
        return FlutterFragment.createDefault()
    }
}

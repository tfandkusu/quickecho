package jp.bellware.echo.view.pager

import android.util.Log
import androidx.fragment.app.Fragment

class EmptyFragment : Fragment() {
    override fun onStart() {
        super.onStart()
        Log.d("EmptyFragment", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("EmptyFragment", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("EmptyFragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("EmptyFragment", "onStop")
    }
}

package jp.bellware.echo.main2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import jp.bellware.echo.R
import jp.bellware.echo.store.MainStore
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.main_control2.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class Main2Activity : AppCompatActivity() {

    private val store: MainStore by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)

        // StoreとViewを繋げる
        store.record.observe(this, Observer {flag ->
            flag?.let {
                if(it)
                    record.show()
                else
                    record.hide()
            }
        })
        store.play.observe(this, Observer {flag ->
            flag?.let {
                if(it)
                    play.show()
                else
                    play.hide()
            }
        })
        store.stop.observe(this, Observer {flag ->
            flag?.let {
                if(it)
                    stop.show()
                else
                    stop.hide()
            }
        })
        store.replay.observe(this, Observer {flag ->
            flag?.let {
                if(it)
                    replay.show()
                else
                    replay.hide()
            }
        })
        store.delete.observe(this, Observer {flag ->
            flag?.let {
                if(it)
                    delete.show()
                else
                    delete.hide()
            }
        })
    }
}

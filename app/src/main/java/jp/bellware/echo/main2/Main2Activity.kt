package jp.bellware.echo.main2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.bellware.echo.R
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)
    }
}

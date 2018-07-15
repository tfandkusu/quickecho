package jp.bellware.echo.setting;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import jp.bellware.echo.R;
import jp.bellware.echo.analytics.AnalyticsHandler;

public class SettingActivity extends AppCompatActivity {

    private AnalyticsHandler ah = new AnalyticsHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //ツールバーの設定
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //フラグメントの追加
        FragmentTransaction ft =getFragmentManager().beginTransaction();
        ft.replace(R.id.preference_frame,new SettingFragment());
        ft.commit();
        //Analytics
        ah.onCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ah.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return false;
    }
}

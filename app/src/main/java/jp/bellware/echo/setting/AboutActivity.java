package jp.bellware.echo.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import jp.bellware.echo.R;
import jp.bellware.echo.analytics.AnalyticsHandler;

public class AboutActivity extends AppCompatActivity {

    private AnalyticsHandler ah = new AnalyticsHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //バージョンを挿入
        TextView versionView = (TextView) findViewById(R.id.version);
        String version = "";
        try{
            PackageManager pm = getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        }catch(PackageManager.NameNotFoundException e){
            //おこらない
        }
        versionView.setText(getString(R.string.version) + " " + version);
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
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}

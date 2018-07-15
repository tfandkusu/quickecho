package jp.bellware.echo.start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import jp.bellware.echo.main.MainActivity;

public class StartActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, PermissionActivity.class);
        startActivity(intent);
        finish();
    }
}

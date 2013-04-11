
package com.nano.lanshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.nano.lanshare.main.BaseActivity;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_bottom);
        startActivity(new Intent(this, BaseActivity.class));
        finish();
    }
}

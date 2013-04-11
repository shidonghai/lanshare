
package com.nano.lanshare.conn.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nano.lanshare.R;

public class ConnectActivity extends Activity implements OnClickListener {
    private Button mBack;
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_status);
        initView();
    }

    private void initView() {
        mBack = (Button) findViewById(R.id.conn_back);
        mBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == mBack) {
            finish();
        }
    }
}

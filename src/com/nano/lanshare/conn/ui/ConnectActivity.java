package com.nano.lanshare.conn.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nano.lanshare.R;

public class ConnectActivity extends FragmentActivity implements
		OnClickListener {
	private Button mBack;
	private FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection_status);
		initView(savedInstanceState);
	}

	private void initView(Bundle savedInstanceState) {
		mBack = (Button) findViewById(R.id.conn_back);
		mBack.setOnClickListener(this);
		if (findViewById(R.id.conn_container) != null) {
			if (savedInstanceState != null) {
				return;
			}
		}
		ConnectionFragment connectionFragment = new ConnectionFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.conn_container, connectionFragment).commit();

	}

	@Override
	public void onClick(View v) {
		if (v == mBack) {
			finish();
		}
	}
}

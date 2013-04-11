package com.nano.lanshare;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.nano.lanshare.apps.ui.AppFragment;

public class MainActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("wyg", "-------------->>");
		setContentView(R.layout.fragment_container);

		this.getSupportFragmentManager().beginTransaction()
				.add(R.id.container, new AppFragment()).commit();
	}
}

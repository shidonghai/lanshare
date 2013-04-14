
package com.nano.lanshare.invitation.ui;

import com.nano.lanshare.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InviteFriendsActivity extends FragmentActivity implements OnClickListener {
    private Button mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_friends);
        initView();
    }

    private void initView() {
        mBack = (Button) findViewById(R.id.invite_back);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBack) {
            finish();
        }
    }
}

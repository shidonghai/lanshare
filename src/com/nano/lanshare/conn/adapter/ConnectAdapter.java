
package com.nano.lanshare.conn.adapter;

import java.util.List;

import com.nano.lanshare.conn.been.UserBean;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ConnectAdapter extends BaseAdapter {
    private List<UserBean> mUserList;

    public ConnectAdapter(List<UserBean> ul) {
        mUserList = ul;
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

}


package com.nano.lanshare.conn.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nano.lanshare.R;
import com.nano.lanshare.components.BasicTabFragment;
import com.nano.lanshare.conn.ui.PullToRefreshListView.OnRefreshListener;

public class ConnectionFragment extends BasicTabFragment {

    private ListView mListView;
    private LayoutInflater mInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void onUpdateData(Message msg) {

    }

    @Override
    protected void init() {
        initLeftView();
        initRightView();
    }

    private void initLeftView() {
        setTitle(LEFT, "aaa(2)");
        View friendsContent = mInflater.inflate(R.layout.connect_people_list, null);
        getGroup(LEFT).addView(friendsContent);
        mListView = (ListView) friendsContent.findViewById(R.id.friends_list);
        ((PullToRefreshListView) mListView).setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetDataTask().execute();
            }
        });
    }

    private void initRightView() {
        setTitle(RIGHT, "bbb(1)");
        View friendsContent = mInflater.inflate(R.layout.connect_hotspots_list, null);
        getGroup(RIGHT).addView(friendsContent);

    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ;
            }
            return new String[] {
                "aa"
            };
        }

        @Override
        protected void onPostExecute(String[] result) {

            // Call onRefreshComplete when the list has been refreshed.
            ((PullToRefreshListView) mListView).onRefreshComplete();

            super.onPostExecute(result);
        }
    }
}

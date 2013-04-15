package com.nano.lanshare.audio.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.audio.logic.IMusicStatusListener;
import com.nano.lanshare.audio.logic.MusicManger;
import com.nano.lanshare.components.operation.OperationDialog;

/**
 * Music tab
 * 
 * @author Xiaohu
 * 
 */
public class MusicTabFragment extends Fragment implements OnItemClickListener,
		IMusicStatusListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.music_tab, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MusicManger manger = MusicManger.getInstance(getActivity());

		MusicListAdapter adapter = new MusicListAdapter(getActivity(),
				manger.getMusicList());
		ListView listView = (ListView) getView().findViewById(R.id.music_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		OperationDialog operationDialog = new OperationDialog(getActivity(),
				OperationDialog.TYPE_MUSIC, null);
		operationDialog.showAsDropDown(arg1);
	}

	@Override
	public void onPrepare() {

	}

	@Override
	public void onComplete() {

	}

	@Override
	public void onError() {

	}
}

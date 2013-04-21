package com.nano.lanshare.file.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.main.LanshareApplication;
import com.nano.lanshare.thumbnail.util.ImageWorker;

public abstract class BasicFileFragment extends Fragment implements
		OnItemClickListener, OnItemLongClickListener {
	public static final int REFRESH_FINISH = 1;
	public static final int SAVE_POSITION = 2;
	public static final int REFRESH = 3;
	public static final int QUERY_RESULT = 4;
	public static final int EXIT_QUERY = 5;

	LayoutInflater mLayoutInflater;
	protected FileListAdapter mAdapter;
	GridView mList;
	View mEmptyView;
	Thread mQueryThread;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			handleMsg(msg);
		}
	};

	public Handler getHandler() {
		return mHandler;
	}

	protected void handleMsg(Message msg) {
		switch (msg.what) {
		case SAVE_POSITION: {
			break;
		}
		case QUERY_RESULT: {
			// mAdapter.setQueryResult((List<File>) msg.obj);
			break;
		}
		case EXIT_QUERY: {
			// mAdapter.exitQueryMode(true);
		}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayoutInflater = inflater;
		View view = mLayoutInflater.inflate(R.layout.fragment_file_list, null);
		mList = (GridView) view.findViewById(R.id.gridview);

		ImageWorker worker = ((LanshareApplication) getActivity()
				.getApplication()).getImageWorker();
		mList.setOnScrollListener(worker.getScrollerListener());
		mEmptyView = (TextView) view.findViewById(R.id.emptyview);

		mAdapter = new FileListAdapter(getActivity());
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(this);
		mList.setOnItemLongClickListener(this);

		return view;
	}

	protected abstract boolean onBackPressed();

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
	}

	protected abstract void setMenuItemVisibility(Menu menu,
			boolean multiSelectionMode);

	protected void setMultiMode(boolean multi) {
		getActivity().supportInvalidateOptionsMenu();
		mAdapter.notifyDataSetChanged();
	}

	protected void exitQueryMode(boolean restore) {
		// mAdapter.exitQueryMode(restore);
		getActivity().supportInvalidateOptionsMenu();
	}

	// public PopupMenu getPopupMenu(View view, Handler handler, List<File>
	// file,
	// OperationListener refreshListener) {
	// PopupMenu menu = new PopupMenu(getActivity(), view);
	// menu.inflate(R.menu.context_menu);
	// menu.setOnMenuItemClickListener(new MenuListener(file, handler,
	// getActivity(), refreshListener));
	// return menu;
	// }

	// public PopupMenu getPopupMenu(View view, Handler handler, File file,
	// OperationListener refreshListener) {
	// List<File> list = new ArrayList<File>();
	// list.add(file);
	// return getPopupMenu(view, handler, list, refreshListener);
	// }
}

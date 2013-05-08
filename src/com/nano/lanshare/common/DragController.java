package com.nano.lanshare.common;

import java.util.ArrayList;

import android.view.ViewGroup;

public class DragController {
	private boolean dragMode;
	private DragView mDragView;
	private static DragController instance;
	private ArrayList<DropTarget> mDropTargets;

	private ViewGroup mBaseView;
	private int x;
	private int y;

	private boolean setPosition;

	private DragController(ViewGroup baseView) {
		mDropTargets = new ArrayList<DropTarget>();
		mBaseView = baseView;
	}

	public static DragController getInstance(ViewGroup baseView) {
		if (instance == null) {
			instance = new DragController(baseView);
		}
		return instance;
	}

	public boolean isDragMode() {
		return dragMode;
	}

	public void dragModeStart(DragView view) {
		dragMode = true;
		mDragView = view;
		mBaseView.addView(view);
		update(x, y);
	}

	public void dragModeEnd() {
		dragMode = false;
		mBaseView.removeView(mDragView);
		mDragView = null;
	}

	public void registerDropTarget(DropTarget target) {
		if (!mDropTargets.contains(target)) {
			mDropTargets.add(target);
		}
	}

	public void unregisterDropTarget(DropTarget target) {
		if (mDropTargets.contains(target)) {
			mDropTargets.remove(target);
		}
	}

	public void update(int x, int y) {
		if (mDragView != null) {
			mDragView.move(x, y);
		} else {
			this.x = x;
			this.y = y;
			setPosition = true;
		}
	}

	public void destroy() {
		instance = null;
	}
}

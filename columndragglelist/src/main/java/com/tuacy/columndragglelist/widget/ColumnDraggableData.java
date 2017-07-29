package com.tuacy.columndragglelist.widget;


import java.util.List;

public class ColumnDraggableData {

	/**
	 * 标题列表
	 */
	private List<String>       mTitle;
	/**
	 * list view 要显示的内容
	 */
	private List<List<String>> mContent;

	public List<String> getTitle() {
		return mTitle;
	}

	public void setTitle(List<String> title) {
		mTitle = title;
	}

	public List<List<String>> getContent() {
		return mContent;
	}

	public void setContent(List<List<String>> content) {
		mContent = content;
	}
}

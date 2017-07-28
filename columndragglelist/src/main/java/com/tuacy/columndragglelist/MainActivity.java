package com.tuacy.columndragglelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tuacy.columndragglelist.widget.ColumnDraggableData;
import com.tuacy.columndragglelist.widget.ColumnDraggableLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private ColumnDraggableLayout mDraggableView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initEvent();
		initData();
	}

	private void initView() {
		mDraggableView = (ColumnDraggableLayout) findViewById(R.id.draggable_list_view);

	}

	private void initEvent() {

	}

	private void initData() {
		mDraggableView.setAdapter(new ColumnAdapter(this, obtainDraggableData()));
	}

	private ColumnDraggableData obtainDraggableData() {
		ColumnDraggableData data = new ColumnDraggableData();
		List<String> title = new ArrayList<>();
		title.add("a");
		title.add("b");
		title.add("c");
		title.add("d");

		List<String> aColumn = new ArrayList<>();
		aColumn.add("column a");
		aColumn.add("column b");
		aColumn.add("column c");
		aColumn.add("column d");
		List<String> bColumn = new ArrayList<>();
		bColumn.add("column a");
		bColumn.add("column b");
		bColumn.add("column c");
		bColumn.add("column d");
		List<List<String>> content = new ArrayList<>();
		content.add(aColumn);
		content.add(bColumn);

		data.setTitle(title);
		data.setContent(content);

		return data;
	}
}

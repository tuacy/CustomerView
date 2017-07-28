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
		List<String> cColumn = new ArrayList<>();
		cColumn.add("column a");
		cColumn.add("column b");
		cColumn.add("column c");
		cColumn.add("column d");
		List<String> dColumn = new ArrayList<>();
		dColumn.add("column a");
		dColumn.add("column b");
		dColumn.add("column c");
		dColumn.add("column d");
		List<String> eColumn = new ArrayList<>();
		eColumn.add("column a");
		eColumn.add("column b");
		eColumn.add("column c");
		eColumn.add("column d");
		List<String> fColumn = new ArrayList<>();
		fColumn.add("column a");
		fColumn.add("column b");
		fColumn.add("column c");
		fColumn.add("column d");
		List<List<String>> content = new ArrayList<>();
		content.add(aColumn);
		content.add(bColumn);
		content.add(cColumn);
		content.add(dColumn);
		content.add(eColumn);
		content.add(fColumn);

		data.setTitle(title);
		data.setContent(content);

		return data;
	}
}

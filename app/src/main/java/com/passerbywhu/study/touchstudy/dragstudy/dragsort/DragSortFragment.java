package com.passerbywhu.study.touchstudy.dragstudy.dragsort;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passerbywhu.study.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuwenchao3 on 2015/7/6.
 */
public class DragSortFragment extends Fragment {
    private DragSortGridView mGridView;
    private DragSortAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drag_sort_fragment, container, false);
        mGridView = (DragSortGridView) view.findViewById(R.id.gridView);
        mAdapter = new DragSortAdapter(getActivity());
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 100; i ++) {
            data.add(i + "");
        }
        mAdapter.setList(data);
        mGridView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        mGridView = null;
        super.onDestroyView();
    }
}

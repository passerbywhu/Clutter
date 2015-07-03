package com.passerbywhu.study.customviewstudy.onmeasure.meausreinfo;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.passerbywhu.study.R;
import com.passerbywhu.study.customviewstudy.onmeasure.MeasureUtils;

import java.util.HashMap;

/**
 * Created by wuwenchao3 on 2015/7/1.
 */
public class LeftDrawerContent {
    private static String[] paramStr = "MATCH_PARENT WRAP_CONTENT EXACTLY".split(" ");
    public static HashMap<Integer, ViewGroup> depthChildMap = new HashMap<Integer, ViewGroup>();
    public static HashMap<Integer, ViewGroupParam> depthChildParamMap = new HashMap<Integer, ViewGroupParam>();

    public static class ViewGroupParam {
        int mode = ViewGroup.LayoutParams.MATCH_PARENT;
        TextView widthView;
        TextView heightView;
    }

    public interface OnParamChangeListener {
        public void onParamChange();
    }

    public static LinearLayout generateLeftDrawerContent(final ViewGroup topLayout, Context context, final OnParamChangeListener onParamChangeListener) {
        depthChildMap.clear();
        depthChildParamMap.clear();
        final int depth = computeDepth(topLayout, 1);
        for (int i = 0; i < depth; i ++) {
            depthChildParamMap.put(i + 1, new ViewGroupParam());
        }
        LinearLayout leftContent = new LinearLayout(context);
        leftContent.setOrientation(LinearLayout.VERTICAL);
        leftContent.setGravity(Gravity.CENTER_HORIZONTAL);
        leftContent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        for (int i = 0; i < depth; i ++) {
            View row = LayoutInflater.from(context).inflate(R.layout.onmeasure_layout_check_item, leftContent, false);
            final ViewGroup child = depthChildMap.get(i + 1);
            final ViewGroupParam childParam = depthChildParamMap.get(i + 1);
            if (child instanceof FrameLayoutWithMeasureInfo) {
                FrameLayoutWithMeasureInfo temp = (FrameLayoutWithMeasureInfo) child;
                ((TextView) row.findViewById(R.id.layoutName)).setText(temp.getName());
            } else if (child instanceof  LinearLayoutWithMeasureInfo) {
                LinearLayoutWithMeasureInfo temp = (LinearLayoutWithMeasureInfo) child;
                ((TextView) row.findViewById(R.id.layoutName)).setText(temp.getName());
            } else if (child instanceof RelativeLayoutWithMeasureInfo) {
                RelativeLayoutWithMeasureInfo temp = (RelativeLayoutWithMeasureInfo) child;
                ((TextView) row.findViewById(R.id.layoutName)).setText(temp.getName());
            }
            View.OnClickListener checkListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.rb_match) {
                        childParam.mode = ViewGroup.LayoutParams.MATCH_PARENT;
                    } else if (v.getId() == R.id.rb_wrap) {
                        childParam.mode = ViewGroup.LayoutParams.WRAP_CONTENT;
                    } else if (v.getId() == R.id.rb_exact) {
                        childParam.mode = 0;
                    }
                }
            };
            row.findViewById(R.id.rb_match).setOnClickListener(checkListener);
            row.findViewById(R.id.rb_wrap).setOnClickListener(checkListener);
            row.findViewById(R.id.rb_exact).setOnClickListener(checkListener);

            childParam.widthView = (TextView) row.findViewById(R.id.widthExactly);
            childParam.heightView = (TextView) row.findViewById(R.id.heightExactly);

            leftContent.addView(row);
        }
        Button button = new Button(context);
        button.setText("Submit");
        button.setBackgroundResource(R.drawable.btn_selector);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeasureUtils.clearMeasureInfo(topLayout);
                for (int i = 0; i < depth; i ++) {
                    final ViewGroup child = depthChildMap.get(i + 1);
                    final ViewGroupParam childParam = depthChildParamMap.get(i + 1);
                    ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
                    if (childParam.mode == ViewGroup.LayoutParams.MATCH_PARENT) {
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        child.setLayoutParams(layoutParams);
                    } else if (childParam.mode == ViewGroup.LayoutParams.WRAP_CONTENT) {
                        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        child.setLayoutParams(layoutParams);
                    } else if (childParam.mode == 0){
                        layoutParams.width = TextUtils.isEmpty(childParam.widthView.getText()) ? 0 : Integer.valueOf(childParam.widthView.getText().toString().trim());
                        layoutParams.height = TextUtils.isEmpty(childParam.heightView.getText()) ? 0 : Integer.valueOf(childParam.heightView.getText().toString().trim());
                        child.setLayoutParams(layoutParams);
                    }
                }
                onParamChangeListener.onParamChange();
            }
        });
        leftContent.addView(button);
        return leftContent;
    }

    private static int computeDepth(ViewGroup layout) {
        int childCount = layout.getChildCount();
        int maxChildDepth = 0;
        for (int i = 0;i < childCount; i ++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                maxChildDepth = Math.max(maxChildDepth, computeDepth((ViewGroup) child));
            }
        }
        return maxChildDepth + 1;
    }

    private static int computeDepth(ViewGroup layout, int depth) {
        int childCount = layout.getChildCount();
        if (depth == 1) {
            depthChildMap.put(depth, layout);
        }
        if (childCount == 0) {
            return depth;
        }
        int maxChildDepth = depth;
        for (int i = 0; i < childCount; i ++) {
            View child = layout.getChildAt(i);
            if (child instanceof  ViewGroup) {
                int curChildTreeDepth = computeDepth((ViewGroup) child, depth + 1);
                if (curChildTreeDepth > maxChildDepth) {
                    maxChildDepth = curChildTreeDepth;
                    depthChildMap.put(depth + 1, (ViewGroup) child);
                }
            }
        }
        return maxChildDepth;
    }
}

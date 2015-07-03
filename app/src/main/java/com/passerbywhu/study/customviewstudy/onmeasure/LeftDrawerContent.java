package com.passerbywhu.study.customviewstudy.onmeasure;

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

import java.util.HashMap;

/**
 * Created by wuwenchao3 on 2015/7/1.
 */
public class LeftDrawerContent {
    private static String[] paramStr = "MATCH_PARENT WRAP_CONTENT EXACTLY".split(" ");
    public static HashMap<Integer, OnMeasureLayout> depthChildMap = new HashMap<Integer, OnMeasureLayout>();
    public static HashMap<Integer, OnMeasureLayoutParam> depthChildParamMap = new HashMap<Integer, OnMeasureLayoutParam>();

    public static class OnMeasureLayoutParam {
        int mode;
        TextView widthView;
        TextView heightView;
    }

    public interface OnParamChangeListener {
        public void onParamChange();
    }

    public static LinearLayout generateLeftDrawerContent(OnMeasureLayout topLayout, Context context, final OnParamChangeListener onParamChangeListener) {
        final int depth = computeDepth(topLayout, 1);
        for (int i = 0; i < depth; i ++) {
            depthChildParamMap.put(i + 1, new OnMeasureLayoutParam());
        }
        LinearLayout leftContent = new LinearLayout(context);
        leftContent.setOrientation(LinearLayout.VERTICAL);
        leftContent.setGravity(Gravity.CENTER_HORIZONTAL);
        leftContent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        for (int i = 0; i < depth; i ++) {
            View row = LayoutInflater.from(context).inflate(R.layout.onmeasure_layout_check_item, leftContent, false);
            final OnMeasureLayout child = depthChildMap.get(i + 1);
            final OnMeasureLayoutParam childParam = depthChildParamMap.get(i + 1);
            ((TextView) row.findViewById(R.id.layoutName)).setText(child.getName());
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
                for (int i = 0; i < depth; i ++) {
                    final OnMeasureLayout child = depthChildMap.get(i + 1);
                    final OnMeasureLayoutParam childParam = depthChildParamMap.get(i + 1);
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

    private static int computeDepth(OnMeasureLayout layout) {
        int childCount = layout.getChildCount();
        int maxChildDepth = 0;
        for (int i = 0;i < childCount; i ++) {
            View child = layout.getChildAt(i);
            if (child instanceof OnMeasureLayout) {
                maxChildDepth = Math.max(maxChildDepth, computeDepth((OnMeasureLayout) child));
            }
        }
        return maxChildDepth + 1;
    }

    private static int computeDepth(OnMeasureLayout layout, int depth) {
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
            if (child instanceof  OnMeasureLayout) {
                int curChildTreeDepth = computeDepth((OnMeasureLayout) child, depth + 1);
                if (curChildTreeDepth > maxChildDepth) {
                    maxChildDepth = curChildTreeDepth;
                    depthChildMap.put(depth + 1, (OnMeasureLayout) child);
                }
            }
        }
        return maxChildDepth;
    }

    //    public static LinearLayout generateLeftDrawerContent(OnMeasureLayout topLayout, Context context) {
////        int depth = computeDepth(topLayout);
//        depthChildMap.put(1, topLayout);
//        int depth = computeDepth(topLayout, 1);
//        LinearLayout linearLayout = new LinearLayout(context);
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        linearLayout.setBackgroundColor(context.getResources().getColor(android.R.color.white));
//        for(int i = 0; i < depth; i ++) {
//            RelativeLayout row = new RelativeLayout(context);
//            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 350));
//            TextView layoutName = new TextView(context);
//            layoutName.setText(depthChildMap.get(i + 1).getName());
//            layoutName.setId(i + 10);
//            layoutName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            row.addView(layoutName);
//            RadioButton rb_match = new RadioButton(context);
//            rb_match.setText("MATCH_PARENT");
//            RadioButton rb_wrap = new RadioButton(context);
//            rb_wrap.setText("WRAP_CONTENT");
//            RadioButton rb_exact = new RadioButton(context);
//            rb_exact.setText("EXACTLY");
//            RadioGroup radioGroup = new RadioGroup(context);
//            radioGroup.setOrientation(LinearLayout.HORIZONTAL);
//            radioGroup.addView(rb_match);
//            radioGroup.addView(rb_wrap);
//            radioGroup.addView(rb_exact);
//            radioGroup.setId(i + 20);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.addRule(RelativeLayout.BELOW, layoutName.getId());
//            radioGroup.setLayoutParams(layoutParams);
//            row.addView(radioGroup);
//            EditText widthEt = new EditText(context);
//            widthEt.setId(i + 30);
//            widthEt.setHint("input width");
//            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.addRule(RelativeLayout.BELOW, radioGroup.getId());
//            widthEt.setLayoutParams(layoutParams);
//            row.addView(widthEt);
//            EditText heightEt = new EditText(context);
//            heightEt.setHint("input height");
//            heightEt.setId(i + 40);
//            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.addRule(RelativeLayout.RIGHT_OF, widthEt.getId());
//            layoutParams.addRule(RelativeLayout.ALIGN_TOP, widthEt.getId());
//            heightEt.setLayoutParams(layoutParams);
//            row.addView(heightEt);
//            View line = new View(context);
//            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            line.setLayoutParams(layoutParams);
//            line.setBackgroundColor(context.getResources().getColor(android.R.color.black));
//            row.addView(line);
//
//            linearLayout.addView(row);
//        }
//        return linearLayout;
//    }
}

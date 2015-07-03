package com.passerbywhu.study.customviewstudy.onmeasure.meausreinfo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.passerbywhu.study.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by wuwenchao3 on 2015/7/3.
 */
public class ColorPickerFragment extends DialogFragment {
    @InjectView(R.id.picker)
    protected ColorPicker colorPicker;
    private int oldColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.color_picker_fragment, container, false);
        ButterKnife.inject(this, view);
        colorPicker.setOnColorSelectedListener(new ColorPicker.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                EventBus.getDefault().post(new MeasureInfoActivity.ColorSelectedEvent(color));
            }
        });
        colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                EventBus.getDefault().post(new MeasureInfoActivity.ColorSelectedEvent(color));
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        colorPicker.setOldCenterColor(oldColor);
        colorPicker.setColor(colorPicker.getColor());
    }

    public void setOldColor(int color) {
        oldColor = color;
    }

    @Override
    public void onDestroyView() {
        colorPicker = null;
        super.onDestroyView();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}

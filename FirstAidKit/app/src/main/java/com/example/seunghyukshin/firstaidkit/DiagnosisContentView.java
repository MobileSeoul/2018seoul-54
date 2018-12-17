package com.example.seunghyukshin.firstaidkit;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DiagnosisContentView extends LinearLayout {
    TextView textView;
    CheckBox Cbox;

    public DiagnosisContentView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.diagnosis_content, this, true);

        textView = (TextView) findViewById(R.id.textView);
        Cbox = (CheckBox) findViewById(R.id.Cbox);
    }

    public void setContent(String Content) {
        textView.setText(Content);
    }

    public void setCheckbox(boolean checked) {
        if (checked) {
            Cbox.setChecked(true);
        } else {
            Cbox.setChecked(false);
        }
    }

}

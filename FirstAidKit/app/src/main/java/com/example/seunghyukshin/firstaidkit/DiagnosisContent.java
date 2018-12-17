package com.example.seunghyukshin.firstaidkit;

import android.widget.CheckBox;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DiagnosisContent {
    String Content;
    boolean Checked;

    public DiagnosisContent(String Content, Boolean Checked) {
        this.Content = Content;
        this.Checked = Checked;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public void setCheckbox(boolean Checked) {
        this.Checked = Checked;
    }

    public String getContent() {
        return Content;
    }

    public boolean getCheckbox() {
        return Checked;
    }
}

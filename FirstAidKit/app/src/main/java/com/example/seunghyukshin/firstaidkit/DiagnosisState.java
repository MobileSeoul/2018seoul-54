package com.example.seunghyukshin.firstaidkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DiagnosisState extends AppCompatActivity {

    TextView textView_symptom;
    TextView lastline;
    Button button_go_to_home;
    Button button_re_diagnosis;

    ImageView imageView_symptom;

    LinearLayout no_symtom;
    LinearLayout has_symtom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_diagnosis);

        Intent intent = getIntent();

        int[] list = intent.getIntArrayExtra("list");

        //Toast.makeText(getApplicationContext(), list[0] + ", " + list[1] + ", " + list[2], Toast.LENGTH_SHORT).show();

        button_go_to_home = (Button) findViewById(R.id.Gotohome);
        button_re_diagnosis = findViewById(R.id.button_re_diagnosis);

        textView_symptom = (TextView) findViewById(R.id.symptom);
        lastline = (TextView) findViewById(R.id.last_text);
        imageView_symptom = (ImageView) findViewById(R.id.symptomImage);
        imageView_symptom.setImageResource(R.drawable.stethoscope);

        no_symtom = findViewById(R.id.no_symtom);
        has_symtom = findViewById(R.id.has_symtom);

        String symptom = "";
        String Lastline = "으로 예상됩니다.";
        for (int i = 0; i < list.length; i++) {

            if (list[0] == 1 && list[9] == 1) {
                symptom = "머리손상";
                imageView_symptom.setImageResource(R.drawable.fa_5);
            } else if (list[14] == 1 && list[15] == 1 && (list[0] == 1 || list[9] == 1) && list[1] == 1) {
                symptom = "알레르기증상";
                imageView_symptom.setImageResource(R.drawable.fa_11);
            } else if (list[0] == 1 && list[2] == 1 && list[6] == 1) {
                symptom = "뇌수막염";
                imageView_symptom.setImageResource(R.drawable.fa_2);
            } else if (list[2] == 1 && (list[1] == 1 || list[4] == 1 || list[9] == 1) && list[3] == 1) {
                symptom = "열사병";
                imageView_symptom.setImageResource(R.drawable.fa_12);
            } else if (list[3] == 1 && (list[8] == 1 || list[6] == 1)) {
                symptom = "저체온증";
                imageView_symptom.setImageResource(R.drawable.fa_13);
            } else if (list[10] == 1 && (list[0] == 1 || list[1] == 1 || list[9] == 1 && list[11] == 1 || list[12] == 1)) {
                symptom = "뇌졸중";
                imageView_symptom.setImageResource(R.drawable.fa_3);
            } else if (list[5] == 1 && list[13] == 1) {
                symptom = "심장질환";
                imageView_symptom.setImageResource(R.drawable.fa_9);
            } else {
                has_symtom.setVisibility(View.GONE);
                no_symtom.setVisibility(View.VISIBLE);

//                symptom = "증상을 알 수 없습니다!\n가까운 병원을 방문해주세요.";
                symptom = "";
                Lastline = "";
            }
        }
        textView_symptom.setText(symptom);
        lastline.setText(Lastline);

        button_go_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiagnosisState.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        button_re_diagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiagnosisState.this, DiagnosisActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
    }
}

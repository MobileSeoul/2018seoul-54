package com.example.seunghyukshin.firstaidkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DiagnosisActivity extends AppCompatActivity {
    ListView listView;
    DiagnosisAdapter adapter;
    Button button_complete;
    Button button_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        button_complete = (Button) findViewById(R.id.button_complete);
        button_cancel = findViewById(R.id.button_diagnosis_cancel);

        listView = (ListView) findViewById(R.id.list_view);
        adapter = new DiagnosisAdapter();

        adapter.addItem(new DiagnosisContent("두통이 있다.", false));//0
        adapter.addItem(new DiagnosisContent("구토증세가 있다.", false));//1
        adapter.addItem(new DiagnosisContent("열이 많이난다.", false));//2
        adapter.addItem(new DiagnosisContent("무력감이 있다.", false));//3
        adapter.addItem(new DiagnosisContent("근육떨림 증세가 있다.", false));//4
        adapter.addItem(new DiagnosisContent("심장을 쥐어짜는 통증이 있다.", false));//5
        adapter.addItem(new DiagnosisContent("오한이 느껴진다.", false));//6
        adapter.addItem(new DiagnosisContent("피부가 건조해 불편함이 느껴진다.", false));//7
        adapter.addItem(new DiagnosisContent("피부가 창백하다.", false));//8
        adapter.addItem(new DiagnosisContent("어지럼증이 있다.", false));//9
        adapter.addItem(new DiagnosisContent("편측마비증상(한쪽 팔다리 혹은 얼굴부분 마비증상)이 있다.", false));//10
        adapter.addItem(new DiagnosisContent("언어장애(실어증; 말을 잘 이해하지 못하거나 표현하는 것이 어려운 장애)가 있다.", false));//11
        adapter.addItem(new DiagnosisContent("시각장애(눈이 나빠졌거나 물체가 두개로 보이는 복시현상)가 있다.", false));//12
        adapter.addItem(new DiagnosisContent("가슴이 답답하거나 아프다.", false));//13
        adapter.addItem(new DiagnosisContent("코가 막히거나 콧물이 난다.", false));//14
        adapter.addItem(new DiagnosisContent("피부에 두드러기나 홍조가 생겼다.", false));//15


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        listView.setAdapter(adapter);

        button_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiagnosisActivity.this, DiagnosisState.class);

                intent.putExtra("list", adapter.getList());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }


    class DiagnosisAdapter extends BaseAdapter {
        ArrayList<DiagnosisContent> items = new ArrayList<DiagnosisContent>();
        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<Boolean> checklist = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(DiagnosisContent item) {
            items.add(item);
            list.add(new Integer(0));
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public int[] getList() {
            int[] temp = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                temp[i] = list.get(i).intValue();
            }
            return temp;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            DiagnosisContentView view = new DiagnosisContentView(getApplicationContext());

            DiagnosisContent item = items.get(position);
            view.setContent((position + 1) + ". " + item.getContent());

            view.Cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    list.set(position, b ? new Integer(1) : new Integer(0));
                }
            });
            if(list.get(position)==1){
                view.Cbox.setChecked(true);
            }else {
                view.setCheckbox(item.getCheckbox());
            }

            return view;
        }
    }
}

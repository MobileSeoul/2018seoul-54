package com.example.seunghyukshin.firstaidkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FirstAidDetailListActivity extends AppCompatActivity {

    ListView listView;
    TextView textView;
    Button button;

    FirstAidAdapter adapter_fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_detail_list);

        textView = findViewById(R.id.first_aid_name);
        listView = findViewById(R.id.listView3);
        button = findViewById(R.id.button_first_aid_detail_cancel);

        adapter_fa = new FirstAidAdapter();

        Bundle extras = getIntent().getExtras();
        String first_aid_name = extras.getString("NAME");
        textView.setText(first_aid_name);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        listView.setAdapter(adapter_fa);

        if(first_aid_name.equals("골절")){
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_0,"척추 골절"));
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_1,"팔 골절"));
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_2,"골반 골절"));
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_3,"발 골절"));
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_4,"쇄골 골절"));
        }
        else if(first_aid_name.equals("기도 폐쇄")){
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_0,"다 막히지 않은 경우"));
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_1,"꽉 막힌 경우"));
        }
        else if(first_aid_name.equals("꽉 막힌 경우")){
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_0,"정신을 잃었을 경우"));
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_1,"정신을 잃지 않았을 경우"));
        }
        else if(first_aid_name.equals("당뇨 응급상황")){
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_0,"저혈당 환자"));
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_1,"고혈당 환자"));
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_2,"판단이 어려운 경우"));
        }
        else if(first_aid_name.equals("저체온증")){
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_0,"저체온증 - 경미할 경우"));
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_1,"저체온증 - 위급할 경우"));
        }
        else if(first_aid_name.equals("중독/해로운 물질")){
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_0,"약물중독"));
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_1,"식중독"));
        }
        else if(first_aid_name.equals("화상")){
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_0,"화재에 의한 화상"));
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_0,"경화상(1도, 2도)"));
            adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_0,"중화상(3도)"));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FirstAidListItem item= (FirstAidListItem) adapter_fa.items.get(position);//선택한 병

                Intent intent;
                String name = item.getName();

                if(name.equals("꽉 막힌 경우")){
                    intent = new Intent( FirstAidDetailListActivity.this, FirstAidDetailListActivity.class);
                }
                else{
                    intent = new Intent( FirstAidDetailListActivity.this, FirstAidActivity.class);
                }

                intent.putExtra("NAME",name);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    class FirstAidAdapter extends BaseAdapter {
        ArrayList<FirstAidListItem> items = new ArrayList<FirstAidListItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(FirstAidListItem item){
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return  position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            FirstAidListItemView2 view = new FirstAidListItemView2(getApplicationContext());

            FirstAidListItem item = items.get(position);
            view.setName(item.getName());
            view.setImage(item.getImage());
            return view;

        }
    }
}

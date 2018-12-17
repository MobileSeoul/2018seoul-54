package com.example.seunghyukshin.firstaidkit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
public class FirstAidListActivity extends AppCompatActivity {
    ListView listView_today_fa; //오늘의 응급상황
    ListView listView_fa;//응급처치법 쭈루룩
    FirstAidAdapter adapter_fa;
    FirstAidAdapter adapter_today_fa;
    LinearLayout layout_fa;

    Button button_first_aid_cancel;

    String dataTemp;
    String dataPop;
    String dataReh;
    String dataWfKor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_list);

        //배경화면색
        layout_fa = (LinearLayout) findViewById(R.id.FirstAidListLayout);
//        layout_fa.setBackgroundColor(Color.rgb(200,191,231));

        listView_today_fa = (ListView) findViewById(R.id.listView);
        adapter_today_fa = new FirstAidAdapter();
        // 우선순위 알고리즘추가 할것

/*
        adapter_today_fa.addItem(new FirstAidListItem("fa_12"));
        listView_today_fa.setAdapter(adapter_today_fa);
*/

        button_first_aid_cancel = findViewById(R.id.button_first_aid_cancel);
        button_first_aid_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        listView_fa = (ListView) findViewById(R.id.listView2);
        //가나다 순으로
        adapter_fa = new FirstAidAdapter();
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_0,"골절"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_1,"기도 폐쇄"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_2,"뇌수막염"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_3,"뇌졸중"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_4,"당뇨 응급상황"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_5,"머리손상"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_6,"무의식"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_7,"발작/간질"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_8,"심리적 응급처치"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_9,"심장발작"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_10,"쏘임/물림"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_11,"알레르기/아나필락시스"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_12,"열사병"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_13,"저체온증"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_14,"정신적 고통"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_15,"좌상/염좌"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_16,"중독/해로운 물질"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_17,"천식발작"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_18,"출혈"));
        adapter_fa.addItem(new FirstAidListItem(R.drawable.fa_19,"화상"));

        listView_fa.setAdapter(adapter_fa);

        listView_today_fa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                goDetailListActivity(true, position);
            }
        });
        listView_fa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                goDetailListActivity(false, position);
            }
        });


        //인텐트 넘어오면서 받아온 날씨정보들
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                dataTemp = null;
                dataPop = null;
                dataReh = null;
                dataWfKor = null;
            } else {
                dataTemp= extras.getString("기온");
                dataPop= extras.getString("강수확률");
                dataReh= extras.getString("습도");
                dataWfKor= extras.getString("구름");
            }
        }else {
            dataTemp= (String) savedInstanceState.getSerializable("기온");
            dataPop= (String) savedInstanceState.getSerializable("강수확률");
            dataReh= (String) savedInstanceState.getSerializable("습도");
            dataWfKor= (String) savedInstanceState.getSerializable("구름");
        }

        setAdapterTodayFA();

    }

    private void goDetailListActivity(boolean today, int position){
        FirstAidListItem item;
        if(today){
            item = (FirstAidListItem) adapter_today_fa.items.get(position);//선택한 병
        }
        else{
            item = (FirstAidListItem) adapter_fa.items.get(position);//선택한 병
        }

        Intent intent;
        String name = item.getName();
        if(name.equals("골절") || name.equals("기도 폐쇄") || name.equals("당뇨 응급상황")
                || name.equals("저체온증") || name.equals("중독/해로운 물질") || name.equals("화상")){
            intent = new Intent( FirstAidListActivity.this, FirstAidDetailListActivity.class);
        }
        else{
            intent = new Intent( FirstAidListActivity.this, FirstAidActivity.class);
        }

        intent.putExtra("NAME",name);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    //가져온 날씨정보로 조건에 맞을시 출력해주도록
    private void setAdapterTodayFA(){
        //if dataTemp>20 일때 fa_12
        if(Double.parseDouble(dataTemp)>=30.0){
            adapter_today_fa.addItem(new FirstAidListItem(R.drawable.fa_12,"열사병"));
            adapter_today_fa.getSharedPreference(0);
        }
        else if(Integer.parseInt(dataPop)>=50){
            adapter_today_fa.addItem(new FirstAidListItem(R.drawable.fa_13,"저체온증"));
            adapter_today_fa.getSharedPreference(0);
        }
        else if(Integer.parseInt(dataReh)>=73){
            adapter_today_fa.addItem(new FirstAidListItem(R.drawable.fa_16,"중독/해로운 물질"));
            adapter_today_fa.getSharedPreference(0);
        }
//        adapter_today_fa.getSharedPreference(0);
        /*
        dataTemp
        dataPop;
        dataReh;
        dataWfKor;*/

        //weather_background.setTag(Integer.valueOf(R.drawable.rain_1));

        listView_today_fa.setAdapter(adapter_today_fa);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            Intent intent =  new Intent(FirstAidListActivity.this,FirstAidActivity.class);
            startActivityForResult(intent,1);

        return super.onOptionsItemSelected(item);
    }


    class FirstAidAdapter extends BaseAdapter{
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
            FirstAidListItemView view = new FirstAidListItemView(getApplicationContext());

            FirstAidListItem item = items.get(position);
            view.setName(item.getName());
            view.setImage(item.getImage());
            return view;

        }

        public void getSharedPreference(int position){

            FirstAidListItem item= (FirstAidListItem) adapter_today_fa.items.get(position);//선택한 병
            String widget_text = item.getName();
            int widget_image = item.getImage();
            SharedPreferences sharedPreferences = getSharedPreferences("com.example.seunghyukshin.firstaidkit.sharedPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("widget_text", widget_text);
            editor.putInt("widget_image",widget_image);

            //editor.putInt("weather_background",(Integer) weather_background.getTag());
            editor.commit();
        }
    }

}

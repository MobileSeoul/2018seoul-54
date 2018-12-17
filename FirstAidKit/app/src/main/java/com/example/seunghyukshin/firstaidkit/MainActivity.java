package com.example.seunghyukshin.firstaidkit;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button button_diag;
    Button button_fa;

    TextView helper;

    TextView tmp;

    TextView textView_pop;
    TextView textView_reh;
    TextView textView_pm10;
    TextView textView_pm25;

    TextView textView_date;
    TextView textView_place;

    ImageView weather_background;

    ImageButton setting;

    public String dataTemp;
    public String dataPop;
    public String dataReh;
    public String dataWfKor;

    public String[] zone_list = {"종로구", "중구", "용산구","성동구","광진구","동대문구", "중랑구",
                            "성북구", "강북구", "도봉구", "노원구", "은평구", "서대문구", "마포구","양천구"
    ,"강서구", "구로구", "금천구", "영등포구", "동작구", "관악구", "서초구", "강남구", "송파구", "강동구"};

    public String[] zone_XY = {"60.127", "60.127", "60.126", "61.127", "62.126", "61.127", "62.128",
            "61.127", "61.128", "61.129", "61.129", "59.127", "59.127", "59.127", "58.126", // 8
            "58.126", "58.125", "59.124", "58.126", "59.125", "59.125", "61.125", "61.126", "62.126", "62.126"}; // 26

    public String[] zone_number = {"11110", "11140", "11170", "11200", "11215", "11230", "11260",
    "11290", "11305", "11320", "11350","11380", "11410", "11440", "11470", "11500", "11530", "11545", "11560",
    "11590", "11620", "11650", "11680", "11710", "11740"};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tmp = findViewById(R.id.tmp);

        textView_pop = findViewById(R.id.pop);
        textView_reh = findViewById(R.id.reh);
        textView_pm10 = findViewById(R.id.pm10);
        textView_pm25 = findViewById(R.id.pm25);

        textView_place = findViewById(R.id.place);
        textView_date = findViewById(R.id.date);
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        String[] day = {"월요일","화요일","수요일","목요일","금요일","토요일","일요일"};
        textView_date.setText((date.getMonth()+1)+"월 " + date.getDate() + "일 " + day[date.getDay()]);

        button_diag = (Button) findViewById(R.id.button_diagnosis);
        button_fa = (Button) findViewById(R.id.button_first_aid);

        setting = findViewById(R.id.setting_btn);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                // 제목셋팅
                alertDialogBuilder.setTitle("지역 설정");

                alertDialogBuilder.setItems(
                        zone_list,  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateWeather(which);
                            }
                        }
                );
                alertDialogBuilder.setNeutralButton("취소", null).show();
            }
        });

        button_diag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DiagnosisActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        button_fa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FirstAidListActivity.class);
                intent.putExtra("기온",dataTemp);
                intent.putExtra("강수확률",dataPop);
                intent.putExtra("습도",dataReh);
                intent.putExtra("구름",dataWfKor);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        weather_background = findViewById(R.id.weather_background);
        helper = findViewById(R.id.helper);

        updateWeather(0);
    }

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Toast.makeText(this, "홈", Toast.LENGTH_SHORT).show();

            return true;
        }
        if (id == R.id.action_setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    String pm10 = "";
    String pm25 = "";
    String pm10value = "";
    String pm25value = "";
    String gradePm10 = "";
    String gradePm25 = "";

    public class ReceiveFineDust extends AsyncTask<URL, Integer, Long>{
        int which;
        String station = "";

        public ReceiveFineDust(int which){
            this.which = which;
            this.station = zone_list[which];
        }

        protected Long doInBackground(URL... urls){
            String service_key = "tuRZOtYGn%2FJEHn9eJqUvaSv9zB5c2%2F53MTDYHIlNdaL%2BKenmHNrGc2ofsIsEytSgVs%2BZLhpkqlVsXsru%2BrfN6Q%3D%3D";

            String url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName="
                    + station
                    + "&dataTerm=daily"
                    + "&pageNo=1&numOfRows=10&ServiceKey="
                    + service_key + "&ver=1.3";

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
                parseXML(response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Long result) {
            gradePm10 = getGrade(pm10);
            gradePm25 = getGrade(pm25);

            textView_pm10.setText(pm10value+"㎍/㎥\n"+gradePm10);
            textView_pm25.setText(pm25value+"㎍/㎥\n"+gradePm25);

            new ReceiveShortestWeather(which).execute();
        }

        String getGrade(String pm){
            if(pm.equals("1")){
                return "좋음";
            }
            else if(pm.equals("2")){
                return "보통";
            }
            else if(pm.equals("3")){
                return "나쁨";
            }
            else if(pm.equals("4")){
                return "매우 나쁨";
            }
            return pm;
        }
        void parseXML(String xml) {
            try {
                String tagName = "";

                boolean onPm10 = true;
                boolean onPm25 = true;
                boolean onPm10value = true;
                boolean onPm25value = true;

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                parser.setInput(new StringReader(xml));

                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                    }
                    else if (eventType == XmlPullParser.TEXT) {
                        if (tagName.equals("pm10Grade1h") && onPm10) {
                            pm10 = parser.getText();
                            onPm10 = false;
                        }
                        if (tagName.equals("pm25Grade1h") && onPm25) {
                            pm25 = parser.getText();
                            onPm25 = false;
                        }
                        if (tagName.equals("pm10Value") && onPm10value) {
                            pm10value = parser.getText();
                            onPm10value = false;
                        }
                        if (tagName.equals("pm25Value") && onPm25value) {
                            pm25value = parser.getText();
                            onPm25value = false;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (tagName.equals("item") && onPm10 == false && onPm25 == false && onPm10value == false && onPm25value == false) {
                            break;
                        }
                    }

                    eventType = parser.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ShortWeather shortWeathers = new ShortWeather();

    public class ReceiveShortWeather extends AsyncTask<URL, Integer, Long> {
        String zone;
        int which;
        public ReceiveShortWeather(int which){
            this.which = which;
            this.zone = zone_number[which];
        }

        protected Long doInBackground(URL... urls) {
            String url = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=" + zone;

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
                parseXML(response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            Log.i("Info ", shortest_lgt + shortest_pty + shortest_reh + shortest_sky + shortest_t1h + shortest_wsd);

            setWeatherData();
            setWeather();
        }

        void parseXML(String xml) {
            try {
                String tagName = "";
                boolean onHour = false;
                boolean onDay = false;
                boolean onTem = false;
                boolean onWfKor = false;
                boolean onPop = false;
                boolean onEnd = false;
                boolean onReh = false;
                boolean isItemTag1 = false;
                boolean onPty = false;

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                parser.setInput(new StringReader(xml));

                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                        if (tagName.equals("data")) {
                            onEnd = false;
                            isItemTag1 = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT && isItemTag1) {
                        if (tagName.equals("hour") && !onHour) {
                            shortWeathers.setHour(parser.getText());
                            onHour = true;
                        }
                        if (tagName.equals("day") && !onDay) {
                            shortWeathers.setDay(parser.getText());
                            onDay = true;
                        }
                        if (tagName.equals("temp") && !onTem) {
                            shortWeathers.setTemp(parser.getText());
                            onTem = true;
                        }
                        if (tagName.equals("wfKor") && !onWfKor) {
                            shortWeathers.setWfKor(parser.getText());
                            onWfKor = true;
                        }
                        if (tagName.equals("pop") && !onPop) {
                            shortWeathers.setPop(parser.getText());
                            onPop = true;
                        }
                        if (tagName.equals("pty") && !onPty) {
                            shortWeathers.setPty(parser.getText());
                            onPty = true;
                        }
                        if (tagName.equals("reh") && !onReh) {
                            shortWeathers.setReh(parser.getText());
                            onReh = true;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (tagName.equals("s06") && onEnd == false) {
                            break;
                        }
                    }

                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    String shortest_t1h = "";
    String shortest_pty = "";
    String shortest_reh = "";
    String shortest_sky = "";
    String shortest_lgt = "";
    String shortest_wsd = "";


    public class ReceiveShortestWeather extends AsyncTask<URL, Integer, Long> {
        int which;
        String x;
        String y;
        String serviceKey = "tuRZOtYGn%2FJEHn9eJqUvaSv9zB5c2%2F53MTDYHIlNdaL%2BKenmHNrGc2ofsIsEytSgVs%2BZLhpkqlVsXsru%2BrfN6Q%3D%3D";

        public ReceiveShortestWeather(int which){
            this.which = which;
            String[] x_y = zone_XY[which].split("\\.");
            this.x = x_y[0];
            this.y = x_y[1];
        }

        protected Long doInBackground(URL... urls) {
            Long current_time = System.currentTimeMillis();
            Date current_date = new Date(current_time);

            String currentTime;

            if(current_date.getMinutes() >= 40){
                currentTime = String.format("%02d", (current_date.getHours())) + "00";
            }
            else{
                currentTime = String.format("%02d", (current_date.getHours() - 1)) + "00";
            }

            String currentDate = "2018" + String.format("%02d", (current_date.getMonth() + 1)) + String.format("%02d", (current_date.getDate()));

            String url = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastGrib?ServiceKey=" +
                    serviceKey +
                    "&base_date=" + currentDate +
                    "&base_time=" + currentTime +
                    "&nx=" + this.x +
                    "&ny=" + this.y +
                    "&pageNo=1&numOfRows=10";

            Log.i("@@@@@@@@@@@@@@@@@@@", url);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
                parseXML(response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            new ReceiveShortWeather(which).execute();
        }


        void parseXML(String xml) {
            try {
                String tagName = "";

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                parser.setInput(new StringReader(xml));

                int eventType = parser.getEventType();

                String tag = "";
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (tagName.equals("category")){
                            tag = parser.getText();
                        }
                        if (tagName.equals("obsrValue")) {
                            if(tag.equals("T1H")){
                                Log.i(tag, parser.getText());
                                shortest_t1h = parser.getText();
                            }
                            if(tag.equals("PTY")){
                                Log.i(tag, parser.getText());

                                shortest_pty = parser.getText();
                            }
                            if(tag.equals("REH")){
                                Log.i(tag, parser.getText());

                                shortest_reh = parser.getText();
                            }
                            if(tag.equals("SKY")){
                                Log.i(tag, parser.getText());

                                shortest_sky = parser.getText();
                            }
                            if(tag.equals("LGT")){
                                Log.i(tag, parser.getText());

                                shortest_lgt = parser.getText();
                            }
                            if(tag.equals("WSD")){
                                Log.i(tag, parser.getText());

                                shortest_wsd = parser.getText();
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (tagName.equals("response")) {
                            break;
                        }
                    }

                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    void setWeatherData(){
        dataTemp = shortest_t1h;
        dataPop = shortWeathers.getPop();
        if(shortest_reh.equals("-998")){
            dataReh = shortWeathers.getReh();
        }
        else{
            dataReh = shortest_reh;
        }
        dataWfKor = shortWeathers.getWfKor();
    }

    void setWeather(){
        tmp.setText(dataTemp + "℃");
        Log.i("@@@@@@@", shortest_pty);
        switch (shortest_pty){
            case "0":
                textView_pop.setText(dataPop + "%");
                break;

            case "1":
                textView_pop.setText("비 오는 중");
                break;

            case "2":
                textView_pop.setText("진눈깨비 오는 중");
                break;

            case "3":
                textView_pop.setText("눈 오는 중");
                break;

            default:
                break;
        }
        textView_reh.setText(dataReh + "%");

        String info = "";
        if(Double.parseDouble(shortest_t1h) >= 40){
            if (shortWeathers.getWfKor().contains("구름")){

            }
            else{
                info += "날이 매우 덥습니다. 열사병을 조심하세요.";
            }
        }
        if(gradePm10.contains("나쁨") || gradePm25.contains("나쁨")){
            info += "미세 먼지가 많습니다! 마스크 꼭 사용하세요.";
        }

        if(Integer.parseInt(dataPop) >= 50){
            if (shortest_pty.equals("1")){
                info += "비가 오고 있어요! 우산을 챙겨주세요.";
            }
            else if(shortest_pty.equals("3")||shortest_pty.equals("2")){
                info += "길이 미끄러울 수 있어요! 조심하세요.";
            }
            else{
                info += "비 올 확률이 높아요! 우산을 챙겨주세요.";
            }
        }
        else{
            if(shortest_sky.equals("1") && !gradePm10.contains("나쁨") && !gradePm25.contains("나쁨")){
                info += "놀러나가기 좋은 날씨네요!";
            }
        }

        long now = System.currentTimeMillis();
        Date date = new Date(now);

        if(shortest_sky.equals("1")){
            if(date.getHours() <= 7 ||20 <= date.getHours()){
                weather_background.setImageResource(R.drawable.moon_1);
            }
            else{
                weather_background.setImageResource(R.drawable.sunny_1);
            }
        }
        else if(shortest_sky.equals("2")){
            weather_background.setImageResource(R.drawable.s_cloud_1);
        }
        else if(shortest_sky.equals("3") || shortest_sky.equals("4")){
            weather_background.setImageResource(R.drawable.m_cloud_1);
        }


        if(shortest_pty.equals("1") || shortest_pty.equals("2")){
            weather_background.setImageResource(R.drawable.rain_1);
        }
        else if(shortest_pty.equals("3")){
            weather_background.setImageResource(R.drawable.snow_1);
        }

        helper.setText(info);
    }

    public void updateWeather(int which){
        textView_place.setText("서울시 " + zone_list[which]);
        new ReceiveFineDust(which).execute();
    }
}

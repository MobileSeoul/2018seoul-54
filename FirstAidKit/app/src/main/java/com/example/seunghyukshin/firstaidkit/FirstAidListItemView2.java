package com.example.seunghyukshin.firstaidkit;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by SEUNGHYUK SHIN on 2018-08-02.
 */

public class FirstAidListItemView2 extends LinearLayout{
    TextView textView;
    ImageView imageView;
    //TextView textView2;
    public FirstAidListItemView2(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.first_aid_list_item_2,this,true);
        textView = (TextView) findViewById(R.id.textView2);
        imageView= (ImageView) findViewById(R.id.imageView2);
        //textView.setTypeface(Typeface.createFromAsset( /*ba.getAssets()*/, "fonts/BMDOHYEON_ttf.ttf"));
    }

    public void setName(String name){
        if(name.length() > 10) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
        }
        textView.setText(name);
    }
    public void setImage(int image){
        imageView.setImageResource(image);
        //imageView.setTag(Integer.valueOf(image));
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView() {
        return textView;
    }
    /*
    public void setContent(String content){
        textView2.setText(content);
    }
    */
}

package com.example.seunghyukshin.firstaidkit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FirstAidListItem {
    int image;
    String name;
    public FirstAidListItem(){

    }
    public FirstAidListItem(int image, String name){
        this.image = image;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }
    /*
    public void setContents(String contents) {
        this.contents = contents;
    }
    public String getContents() {
        return contents;
    }
*/
}

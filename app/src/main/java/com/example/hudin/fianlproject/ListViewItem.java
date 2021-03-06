package com.example.hudin.fianlproject;

import android.graphics.drawable.Drawable;

import static com.example.hudin.fianlproject.R.drawable.mon;
import static com.example.hudin.fianlproject.R.drawable.tue;
import static com.example.hudin.fianlproject.R.drawable.wed;
import static com.example.hudin.fianlproject.R.drawable.thu;
import static com.example.hudin.fianlproject.R.drawable.fri;
import static com.example.hudin.fianlproject.R.drawable.sat;
import static com.example.hudin.fianlproject.R.drawable.sun;


/**
 * Created by hudin on 2017-06-15.
 */

public class ListViewItem {
    private Drawable iconDrawable ;
    private String titleStr ;
    private String descStr ;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }

}

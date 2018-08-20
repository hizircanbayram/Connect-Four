package com.example.xxx.connectfourv3;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class Cell extends Button {

    private int x;
    private int y;
    private int buttonColor;

    public Cell(Context context, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
    }


    public void setCellColorFilter(int color) {
        buttonColor = color;
    }

    public int getCellColorFilter() {
        return buttonColor;
    }

    public int getNoX() {
        return x;
    }

    public int getNoY() { return y;}
}

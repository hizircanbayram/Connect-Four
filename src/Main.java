package com.example.xxx.connectfourv3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main extends AppCompatActivity implements View.OnClickListener{

    private Button increaseSize;
    private Button decreaseSize;
    private Button increaseTime;
    private Button decreaseTime;
    private Button play;
    private TextView firstMessage;
    private TextView timeAmount;
    private TextView boardAmount;
    private TextView sizeView;
    private TextView timeView;
    private String gameModeTemp = new String();
    private int size = 5;
    private int time = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        increaseTime = findViewById(R.id.increaseTime);
        increaseSize  = findViewById(R.id.increaseSize);
        decreaseTime = findViewById(R.id.decreaseTime);
        decreaseSize = findViewById(R.id.decreaseSize);
        boardAmount = findViewById(R.id.boardAmount);
        timeAmount = findViewById(R.id.timeAmount);
        firstMessage = findViewById(R.id.firstMessage);
        sizeView = findViewById(R.id.sizeView);
        timeView = findViewById(R.id.userName);
        play = findViewById(R.id.play);
        play.setOnClickListener(this);
        increaseSize.setOnClickListener(this);
        increaseTime.setOnClickListener(this);
        decreaseSize.setOnClickListener(this);
        decreaseTime.setOnClickListener(this);
        Intent connectWithGameMode = getIntent();
        gameModeTemp = connectWithGameMode.getStringExtra("gameMode");
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == increaseTime.getId()) {
            if (time < 40)
                ++time;
            else
                time = 5;
        }
        else if (view.getId() == decreaseTime.getId()) {
            if (time > 3)
                --time;
            else
                time = 40;
        }
        else if (view.getId() == increaseSize.getId()) {
            if (size < 40)
                ++size;
            else
                size = 5;
        }
        else if (view.getId() == decreaseSize.getId()) {
            if (size > 5)
                --size;
            else
                size = 40;
        }
        else if (view.getId() == play.getId()) {
            finish();
            Intent userNameMessage = new Intent(Main.this, PlayScreen.class);
            userNameMessage.putExtra("amountTime", time);
            userNameMessage.putExtra("boardSize", size);
            userNameMessage.putExtra("gameMode", gameModeTemp);
            startActivity(userNameMessage);
        }
        sizeView.setText(String.valueOf(size));
        timeView.setText(String.valueOf(time));
    }
}
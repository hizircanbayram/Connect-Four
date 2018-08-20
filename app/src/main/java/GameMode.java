package com.example.xxx.connectfourv3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class GameMode extends AppCompatActivity implements View.OnClickListener {

    private Button pvpButton;
    private Button pvcButton;
    private String gameMode;
    private static String pvpMode = "1";
    private static String pvcMode = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        pvpButton = findViewById(R.id.pvp);
        pvcButton = findViewById(R.id.pvc);
        pvpButton.setOnClickListener(this);
        pvcButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == pvpButton.getId())
            gameMode = pvpMode;
        else if (view.getId() == pvcButton.getId())
            gameMode = pvcMode;
        Intent gameModeMessage = new Intent(GameMode.this, Main.class);
        gameModeMessage.putExtra("gameMode", gameMode);
        finish();
        startActivity(gameModeMessage);
    }
}


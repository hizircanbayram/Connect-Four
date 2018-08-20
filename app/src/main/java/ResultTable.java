package com.example.xxx.connectfourv3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Process;

public class ResultTable extends AppCompatActivity implements View.OnClickListener {

    private String winMessage;
    private TextView wmTextView;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_table);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent connectWithPlayScreen = getIntent();
        this.winMessage = connectWithPlayScreen.getStringExtra("resultTable");
        wmTextView = findViewById(R.id.wmTextView);
        wmTextView.setText(winMessage);
        exit = findViewById(R.id.exit);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == exit.getId())
            finish();

    }

    @Override
    protected void onDestroy() {
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }
}

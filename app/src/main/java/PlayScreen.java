package com.example.xxx.connectfourv3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;


public class PlayScreen extends AppCompatActivity implements View.OnClickListener {

    private Board b;
    private Button undoButton;
    private TextView textViewthreadCount;
    private GridLayout gridManager;
    private LinearLayout optionsLayout;

    Handler randomMove = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int randomX = 0, randomY = 0;
            do {
                Random rand = new Random();
                randomX = rand.nextInt(b.getBoardSize());
                randomY = rand.nextInt(b.getBoardSize());
            }while(!b.isRandomCoordinatesSuccessfull(randomX, randomY, gridManager));

            if (b.getTurn() % 2 == 0) {
                b.setCell(randomX, randomY, b.getPlayer1Color(), gridManager);
                b.putMovesOntoStack(randomX, randomY, b.getPlayer1Color(), gridManager);
                if (b.checkWinnerPatterns(gridManager) == -1)
                    goResultTable("Player2 has won!");
                else if (b.checkWinnerPatterns(gridManager) == 1)
                    goResultTable("Player1 has won!");
            }
            else {
                b.setCell(randomX, randomY, b.getPlayer2Color(), gridManager);
                b.putMovesOntoStack(randomX, randomY, b.getPlayer2Color(), gridManager);
                if (b.checkWinnerPatterns(gridManager) == -1)
                    goResultTable("Player2 has won!");
                else if (b.checkWinnerPatterns(gridManager) == 1)
                    goResultTable("Player1 has won!");
            }

            if (b.gridFull(gridManager)== true) {
                String resultMessage = new String("Grid is full!");
                goResultTable(resultMessage);
            }
            b.changeTurn();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        undoButton = findViewById(R.id.undo);
        undoButton.setOnClickListener(this);
        textViewthreadCount = findViewById(R.id.textViewthreadCount);
        textViewthreadCount.setTextColor((Color.GREEN));
        optionsLayout = findViewById(R.id.optionsLayout);
        optionsLayout.setBackgroundColor(Color.BLACK);
        
        Intent connectWithMain = getIntent();
        
        if (savedInstanceState == null) {
            b = new Board();
            b.setGameMode(Integer.parseInt(connectWithMain.getStringExtra("gameMode")));
            b.setBoardSize(connectWithMain.getIntExtra("boardSize", 0));
            b.setMoveTime(connectWithMain.getIntExtra("amountTime", 0));
            b.setField();
            startTiming();
            setGridManager();
        }
        else {
            b = savedInstanceState.getParcelable("board");
            setGridManager();
            startTiming();
            filledInGrid();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("board", b);
    }

    public void filledInGrid() {
        for (Cell c : b.getDataAux()) {
            int x = c.getNoX();
            int y = c.getNoY();
            int color = c.getCellColorFilter();
            b.setCell(x, y, color, gridManager);
        }
    }

    public synchronized void startTiming() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (b.ismStopLoop()){
                    try {
                        Thread.sleep(1000);
                        b.increaseCount();
                        if (b.getCount() == b.getMoveTime() && !b.isMoveMadeBeforeTiming()) {
                            randomMove.sendEmptyMessage(0);//player1
                            if (b.getGameMode() == 0) {
                                randomMove.sendEmptyMessage(0);//player2
                                b.checkWinnerPatterns(gridManager);
                            }
                        }
                        if (b.getCount() == b.getMoveTime())
                            b.setCount(0);
                    } catch (InterruptedException e) {}
                    textViewthreadCount.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewthreadCount.setText(" " + b.getCount());
                        }
                    });

                }
            }
        };
        Thread mythread = new Thread(r);
        mythread.start();
    }

    
    public GridLayout getGridManager() {
        return gridManager;
    }
    

    public void setGridManager() {
        gridManager = findViewById(R.id.gridManager);
        gridManager.setColumnCount(b.getBoardSize());
        gridManager.setRowCount(b.getBoardSize());
        gridManager.setBackgroundColor(Color.BLACK);

        for (int i = 0; i < b.getBoardSize(); ++i) {
            for (int k = 0; k < b.getBoardSize(); ++k) {
                gridManager.addView(new Cell(this, i, k));
                gridManager.getChildAt(i * b.getBoardSize() + k).setOnClickListener(this);
                gridManager.getChildAt(i * b.getBoardSize() + k).setBackgroundResource(R.drawable.round_button);
                b.setCell(i, k, b.getDefaultColor(), gridManager);
            }
        }
    }


    public void goResultTable(String message) {
        Intent resultMessage = new Intent(PlayScreen.this, ResultTable.class);
        resultMessage.putExtra("resultTable", message);
        finish();
        startActivity(resultMessage);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == undoButton.getId() && b.getTurn() > 0) {
            if (b.getGameMode() == 0) {
                b.undoLastMove(gridManager);
                b.undoTurn();
                //computerMove(); unlimitid demek hepsini geri almaksa bu satirlar olmayacak
                //changeTurn();
            }
            else {
                b.undoLastMove(gridManager);
                b.undoTurn();
            }
        }
        else if (view.getId() != undoButton.getId()){
            Cell a = (Cell)view;
            int buttonNoY = a.getNoY();
            b.setMoveMadeBeforeTiming(true);
            if (b.getCount() == b.getMoveTime() || (b.getCount() < b.getMoveTime() && b.isMoveMadeBeforeTiming()))
                b.setCount(-1); //threadlerden birinde 1 artiriyor. bundan dolayi -1 verdim ki, 0 dan baslasin
            for (int i = b.getBoardSize() - 1; i >= 0; --i){
                if (b.getCell(i, buttonNoY, gridManager) == b.getDefaultColor()){
                    if ((b.getTurn() % 2 == 0) && (b.getGameMode() == 1)) {
                        b.setCell(i, buttonNoY, b.getPlayer1Color(), gridManager);
                        b.putMovesOntoStack(i, buttonNoY, b.getPlayer1Color(), gridManager);
                        b.changeTurn();
                        int val = b.checkWinnerPatterns(gridManager);
                        if (val == -1 || val == 1) {
                            if (b.getTurn() % 2 == 1)
                                goResultTable("Player1 has won!");
                            else
                                goResultTable("Player2 has won!");
                        }

                    }
                    else if ((b.getTurn() % 2 == 1) && (b.getGameMode() == 1)) {
                        b.setCell(i, buttonNoY, b.getPlayer2Color(), gridManager);
                        b.putMovesOntoStack(i, buttonNoY, b.getPlayer2Color(), gridManager);
                        b.changeTurn();
                        int val = b.checkWinnerPatterns(gridManager);
                        if (val == -1 || val == 1) {
                            if (b.getTurn() % 2 == 1)
                                goResultTable("Player1 has won!");
                            else
                                goResultTable("Player2 has won!");
                        }

                    }
                    else if ((b.getTurn() % 2 == 0) && (b.getGameMode() == 0)) {
                        b.setCell(i, buttonNoY, b.getPlayer1Color(), gridManager);
                        if (b.gridFull(gridManager) == true) {
                            String resultMessage = new String("Grid is full!");
                            goResultTable(resultMessage);
                        }
                        b.putMovesOntoStack(i, buttonNoY, b.getPlayer1Color(), gridManager);
                        b.changeTurn();
                        int val = b.checkWinnerPatterns(gridManager);
                        if (val == -1 || val == 1) {
                            if (b.getTurn() % 2 == 1)
                                goResultTable("Player1 has won!");
                            else
                                goResultTable("Player2 has won!");
                        }
                        b.randomComputerMove(gridManager);
                        b.changeTurn();
                        int val2 = b.checkWinnerPatterns(gridManager);
                        if (val2 == -1 || val2 == 1) {
                            if (b.getTurn() % 2 == 1)
                                goResultTable("Player1 has won!");
                            else
                                goResultTable("Player2 has won!");
                        }
                    }
                    break;
                }
            }
            if (b.gridFull(gridManager)== true) {
                String resultMessage = new String("Grid is full!");
                goResultTable(resultMessage);
            }
        }
    }
}

package com.example.xxx.connectfourv3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Created by xxx on 2/23/2018.
 */

public class Board implements Parcelable {
    private int CON_NUMBER = 4;
    private int player1Color = Color.BLUE;
    private int player2Color = Color.RED;
    private int winningColor = Color.BLACK;
    private int defaultColor = Color.MAGENTA;
    private String player1won = new String("Player 1 has won!");
    private String player2won = new String("Player 2 has won!");

    private Stack<Cell> moves;
    private ArrayList<Cell> dataAux;
    private boolean moveMadeBeforeTiming;
    private boolean mStopLoop;
    private int count;
    private int turn;
    private boolean isGameOver;
    private int dataAuxInt;

    private int gameMode;
    private int boardSize;
    private int moveTime;


    public Board() {
        moves = new Stack<>();
        dataAux = new ArrayList<>();
        this.turn = 0;
        this.count = 0;
        this.mStopLoop = true;
        this.moveMadeBeforeTiming = false;
        this.isGameOver = false;
        this.dataAuxInt = 0;
    }

    public boolean isRandomCoordinatesSuccessfull(int randomX, int randomY, GridLayout gridManager) {
        Cell c = (Cell) gridManager.getChildAt(randomX * boardSize + randomY);
        Cell c1;
        if (randomX != boardSize - 1) {
            c1 = (Cell) gridManager.getChildAt((randomX + 1) * boardSize + randomY);
            if (c.getCellColorFilter() == defaultColor && (c1.getCellColorFilter() == player1Color  || c1.getCellColorFilter() == player2Color))
                return true;
        }
        else if (c.getCellColorFilter() == defaultColor)
            return true;
        return false;
    }

    public boolean gridFull(GridLayout gridManager)
    {
        int cnt = 0;
        for (int i = 0; i < boardSize; ++i)
            for (int k = 0; k < boardSize; ++k) {
                Cell c = (Cell) gridManager.getChildAt(i * boardSize + k);
                if (c.getCellColorFilter() != defaultColor)
                    ++cnt;
            }
        return (cnt == (boardSize * boardSize)) ? true : false;
    }

    public GridLayout getGridManager(GridLayout gridManager) {
        return gridManager;
    }

    public int getPlayer1Color() {
        return player1Color;
    }

    public int getPlayer2Color() {
        return player2Color;
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public int getWinningColor() {
        return winningColor;
    }

    public int getCount() {
        return count;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public void setMoveTime(int moveTime) {
        this.moveTime = moveTime;
    }

    public boolean ismStopLoop() {
        return mStopLoop;
    }

    public boolean isMoveMadeBeforeTiming() {
        return moveMadeBeforeTiming;
    }

    public void setMoveMadeBeforeTiming(boolean moveMadeBeforeTiming) {
        this.moveMadeBeforeTiming = moveMadeBeforeTiming;
    }

    public int getMoveTime() {
        return moveTime;
    }

    public int getGameMode() {
        return gameMode;
    }

    public Stack<Cell> getMoves() {
        return moves;
    }

    public ArrayList<Cell> getDataAux() {
        return dataAux;
    }

    /**
     * lets you know whether the game is over or not
     * @return isGameOver
     */
    public boolean getGameOver()
    {
        return isGameOver;
    }

    /**
     * lets you know to set the situation for game
     * @param go will be set to isGameOver
     */
    public void setGameOver(boolean go)
    {
        isGameOver = go;
    }

    /**
     * get width of the board
     * @return boardSize of game
     */
    public int getBoardSize()
    {
        return boardSize;
    }

    public String getPlayer1won() {
        return player1won;
    }

    public String getPlayer2won() {
        return player2won;
    }

    /**
     * get whose turn
     * @return game turn
     */
    public int getTurn()
    {
        return turn;
    }

    public int getCON_NUMBER() {
        return CON_NUMBER;
    }

    public void increaseCount() {
        ++count;
    }

    /**
     * changes turn of the game
     */
    public synchronized void changeTurn() {
        ++turn;
    }

    public synchronized void undoTurn() {
        --turn;
    }

    /**
     * set cell
     * @param x (indice of button)
     * @param color (color type to be coloured)
     */
    public void setCell(int x, int y, int color, GridLayout gridManager)
    {
        Cell c = (Cell) gridManager.getChildAt(x * boardSize + y);
        c.setCellColorFilter(color);
        gridManager.getChildAt(x * boardSize + y).getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        moveMadeBeforeTiming = false;
    }

    public int getCell(int k, int i, GridLayout gridManager) {
        Cell c = (Cell) gridManager.getChildAt(k * boardSize + i);
        return c.getCellColorFilter();
    }

    /**
     * check the game whether there is a path horizontally to end the game
     * @return 1 (for green player)
     * @return -1 (for yellow player)
     * @return 0 (no any path)
     */
    public int checkHorizantal(GridLayout gridManager)
    {
        for (int k = 0; k < boardSize; ++k) {
            for (int i = 0; i < boardSize - CON_NUMBER + 1; ++i) {
                if (getCell(k, i, gridManager)  == player1Color &&
                        getCell(k, i + 1, gridManager) == player1Color &&
                        getCell(k , i + 2, gridManager) == player1Color &&
                        getCell(k, i + 3, gridManager) == player1Color)
                {
                    for (int m = 0; m < CON_NUMBER; ++m)
                        setCell(k, i + m, winningColor, gridManager);
                    return 1;
                    //goResultTable(player1won);
                }
                else if (getCell(k, i, gridManager)  == player2Color &&
                        getCell(k, i + 1, gridManager) == player2Color &&
                        getCell(k , i + 2, gridManager) == player2Color &&
                        getCell(k, i + 3, gridManager) == player2Color)
                {
                    for (int m = 0; m < CON_NUMBER; ++m)
                        setCell(k, i + m, winningColor, gridManager);
                    return -1;
                    //goResultTable(player2won);
                }

            }
        }
        return 0;
    }


    /**
     * check the game whether there is a parth vertically to end the game
     * @return 1 (for green player)
     * @return -1 (for yellow player)
     * @return 0 (no any path)
     */
    public int checkVertical(GridLayout gridManager)
    {
        for (int i = 0; i < boardSize; ++i) {
            for (int k = 0; k < boardSize - CON_NUMBER + 1; ++k) {
                if (getCell(k, i, gridManager)  == player1Color &&
                        getCell(k + 1, i, gridManager) == player1Color &&
                        getCell(k + 2 , i, gridManager) == player1Color &&
                        getCell(k + 3, i, gridManager) == player1Color) {
                    for (int m = 0; m < CON_NUMBER; ++m)
                        setCell(k + m, i, winningColor, gridManager);
                    return 1;
                    //goResultTable(player1won);
                }
                else if (getCell(k, i, gridManager)  == player2Color &&
                        getCell(k + 1, i, gridManager) == player2Color &&
                        getCell(k + 2 , i, gridManager) == player2Color &&
                        getCell(k + 3, i, gridManager) == player2Color) {
                    for (int m = 0; m < CON_NUMBER; ++m)
                        setCell(k + m, i, winningColor, gridManager);
                    //goResultTable(player2won);
                    return -1;
                }
            }
        }
        return 0;
    }

    /**
     * check the game whether there is a parth right diagonally to end the game
     * @return 1 (for green player)
     * @return -1 (for yellow player)
     * @return 0 (no any path)
     */
    public int checkRightDiagonal(GridLayout gridManager)
    {
        for (int i = 0; i < boardSize; ++i) {
            for (int k = 0; k < boardSize; ++k) {
                if (i >= 0 && i < (boardSize - CON_NUMBER + 1) && k >= 0 && k < (boardSize - CON_NUMBER + 1)) {
                    if (getCell(k, i, gridManager) == player1Color &&
                            getCell(k + 1, i + 1, gridManager)  == player1Color &&
                            getCell(k + 2 , i + 2, gridManager) == player1Color &&
                            getCell(k + 3, i + 3, gridManager)  == player1Color) {
                        for (int m = 0; m < CON_NUMBER; ++m)
                            setCell(k + m, i + m, winningColor, gridManager);
                        return 1;
                        //goResultTable(player1won);
                    }
                    else if (getCell(k, i, gridManager)  == player2Color &&
                            getCell(k + 1, i + 1, gridManager) == player2Color &&
                            getCell(k + 2 , i + 2, gridManager) == player2Color &&
                            getCell(k + 3, i + 3, gridManager) == player2Color) {
                        for (int m = 0; m < CON_NUMBER; ++m)
                            setCell(k + m, i + m, winningColor, gridManager);
                        return -1;
                        //goResultTable(player2won);
                    }
                }
            }
        }
        return 0;
    }

    /**
     * check the game whether there is a path left diagonally to end the game
     * @return 1 (for green player)
     * @return -1 (for gray player)
     * @return 0 (no any path)
     */
    public int checkLeftDiagonal(GridLayout gridManager)
    {
        for (int i = 0; i < boardSize; ++i){
            for (int k = 0; k < boardSize; ++k){
                if (i - 3 >= 0 && i < boardSize - 1 && k < (boardSize - CON_NUMBER + 1) && k >= 0){
                    if (getCell(k, i, gridManager)  == player1Color &&
                            getCell(k + 1, i - 1, gridManager) == player1Color &&
                            getCell(k + 2 , i - 2, gridManager) == player1Color &&
                            getCell(k + 3, i - 3, gridManager) == player1Color) {
                        for (int m = 0; m < CON_NUMBER; ++m)
                            setCell(k + m, i - m, winningColor, gridManager);
                        return 1;
                        //goResultTable(player1won);
                    }
                    else if (getCell(k, i, gridManager)  == player2Color &&
                            getCell(k + 1, i - 1, gridManager) == player2Color &&
                            getCell(k + 2 , i - 2, gridManager) == player2Color &&
                            getCell(k + 3, i - 3, gridManager) == player2Color) {
                        for (int m = 0; m < CON_NUMBER; ++m)
                            setCell(k + m, i - m, winningColor, gridManager);
                        return -1;
                        //goResultTable(player2won);
                    }
                }
            }
        }
        return 0;
    }


    public void setField() {
        moves = new Stack<>();
        this.turn = 0;
        this.count = 0;
        this.mStopLoop = true;
        this.moveMadeBeforeTiming = false;
    }


    public int checkWinnerPatterns(GridLayout gridManager) {
        int cv = checkVertical(gridManager);
        if (cv != 0)
            return cv;

        int ch = checkHorizantal(gridManager);
        if (ch != 0)
            return ch;

        int cl = checkLeftDiagonal(gridManager);
        if (cl != 0)
            return cl;

        int cr = checkRightDiagonal(gridManager);
        if (cr != 0)
            return cr;

        return 0;
    }


    public void undoLastMove(GridLayout gridManager) {
        Cell undoElement = moves.pop();
        setCell(undoElement.getNoX(), undoElement.getNoY(), defaultColor, gridManager);
        dataAux.remove(dataAuxInt - 1);
        --dataAuxInt;
    }

    public void putMovesOntoStack(int x, int y, int color, GridLayout gridManager) {
        Cell element = (Cell) gridManager.getChildAt(x * boardSize + y);
        moves.push(element);
        dataAux.add(dataAuxInt, element);
        ++dataAuxInt;
    }

    public synchronized void setCount(int val) {
        this.count = val;
    }

    public void randomComputerMove(GridLayout gridManager) {
        int randomX = 0, randomY = 0;
        do {
            Random rand = new Random();
            randomX = rand.nextInt(boardSize);
            randomY = rand.nextInt(boardSize);
        }while(!isRandomCoordinatesSuccessfull(randomX, randomY, gridManager));
        setCell(randomX, randomY, player2Color, gridManager);
        putMovesOntoStack(randomX, randomY, player2Color, gridManager);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.CON_NUMBER);
        dest.writeInt(this.player1Color);
        dest.writeInt(this.player2Color);
        dest.writeInt(this.winningColor);
        dest.writeInt(this.defaultColor);
        dest.writeString(this.player1won);
        dest.writeString(this.player2won);
        dest.writeList(this.moves);
        dest.writeList(this.dataAux);
        dest.writeByte(this.moveMadeBeforeTiming ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mStopLoop ? (byte) 1 : (byte) 0);
        dest.writeInt(this.count);
        dest.writeInt(this.turn);
        dest.writeByte(this.isGameOver ? (byte) 1 : (byte) 0);
        dest.writeInt(this.dataAuxInt);
        dest.writeInt(this.gameMode);
        dest.writeInt(this.boardSize);
        dest.writeInt(this.moveTime);
    }

    protected Board(Parcel in) {
        this.CON_NUMBER = in.readInt();
        this.player1Color = in.readInt();
        this.player2Color = in.readInt();
        this.winningColor = in.readInt();
        this.defaultColor = in.readInt();
        this.player1won = in.readString();
        this.player2won = in.readString();
        this.moves = new Stack<>();
        in.readList(this.moves, Cell.class.getClassLoader());
        this.dataAux = new ArrayList<>();
        in.readList(this.dataAux, Cell.class.getClassLoader());
        this.moveMadeBeforeTiming = in.readByte() != 0;
        this.mStopLoop = in.readByte() != 0;
        this.count = in.readInt();
        this.turn = in.readInt();
        this.isGameOver = in.readByte() != 0;
        this.dataAuxInt = in.readInt();
        this.gameMode = in.readInt();
        this.boardSize = in.readInt();
        this.moveTime = in.readInt();
    }

    public static final Creator<Board> CREATOR = new Creator<Board>() {
        @Override
        public Board createFromParcel(Parcel source) {
            return new Board(source);
        }

        @Override
        public Board[] newArray(int size) {
            return new Board[size];
        }
    };
}

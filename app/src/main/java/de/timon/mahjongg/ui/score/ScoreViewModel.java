package de.timon.mahjongg.ui.score;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScoreViewModel extends ViewModel {

    public boolean gameInProgress = false;
    public String[] names = new String[4];
    public int east = -1;
    public int currentRound = 0;
    public int playerCount = 4;
    public int [] [] scores = new int[4][2];
    public boolean tempValue = false;
    public View view = View.NEWGAME;

    protected enum View {
        INGAME,
        NEWGAME,
        INPUTNAMES
    }
    //q: which is for row, which is for column of the []
    //a: first [] is for row, second [] is for column
}
package de.timon.mahjongg.ui.score;

import androidx.lifecycle.ViewModel;

import de.timon.mahjongg.ApplicationGlobals;

public class ScoreViewModel extends ViewModel {

    private final String[] names = new String[4];
    public int east = -1;
    private int [] [] scores = {{0,0,0}, {0,0,0}, {0,0,0}, {0,0,0}};
    public View view = View.NEWGAME;

    public String getName(int player) {
        return names[player];
    }

    public void startNewGame(String name0, String name1, String name2, String name3, int east) {
        names[0] = name0;
        names[1] = name1;
        names[2] = name2;
        names[3] = name3;
        if (name3.equals(""))
            ApplicationGlobals.playerCount = 3;
        else
            ApplicationGlobals.playerCount = 4;
        this.east = east;
    }

    public void loadGame() {
        //TODO: load game from file
    }

    public void addPoints(int[] points, int mahjongg) {

        if (mahjongg == -1)
            return;

        int[] pre = new int[ApplicationGlobals.playerCount];
        for (int i = 0; i < ApplicationGlobals.playerCount; i++)
            pre[i] = scores[i][0];

        scores[mahjongg][2]++;

        for (int i = 0; i < ApplicationGlobals.playerCount; i++) {
            if (i != mahjongg) {
                scores[mahjongg][0] += points[mahjongg];
                scores[i][0] -= points[mahjongg];
                if (mahjongg == east) {
                    scores[mahjongg][0] += points[mahjongg];
                    scores[i][0] -= points[mahjongg];
                } else if (i == east) {
                    scores[mahjongg][0] += points[mahjongg];
                    scores[i][0] -= points[mahjongg];
                }
            }
        }

        //TODO: berechne andere punktevergabe

        for (int i = 0; i < ApplicationGlobals.playerCount; i++)
            scores[i][1] = scores[i][0] - pre[i];

        if (mahjongg != east)
            east = (east + 1) % ApplicationGlobals.playerCount;
    }

    public int getScore(int player) {
        return scores[player][0];
    }

    public int getDifference(int player) {
        return scores[player][1];
    }

    public int getMahjonggCount(int player) {
        return scores[player][2];
    }

    protected enum View {
        INGAME,
        NEWGAME,
        INPUTNAMES
    }
    //q: which is for row, which is for column of the []
    //a: first [] is for row, second [] is for column


}
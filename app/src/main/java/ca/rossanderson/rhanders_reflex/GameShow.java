package ca.rossanderson.rhanders_reflex;

import android.content.Context;

/**
 * Created by ross on 15-09-28.
 */
public class GameShow {
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    private int numPlayers;

    public void saveGameShowBuzz(Integer player, Context cxt) {
        StatsModel.getStatsModel().saveGameShowBuzz(this.getNumPlayers(), player, cxt);
    }

}

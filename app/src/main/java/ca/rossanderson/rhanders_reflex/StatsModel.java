package ca.rossanderson.rhanders_reflex;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

/**
 * Created by ross on 15-09-27.
 */
public class StatsModel {
    private String REACTION_TIMES_FILENAME = "statsRT.json";
    private String GAME_SHOW_FILENAME = "statsGS.json";

    private static StatsModel instance;

    private StatsModel() { }
    public static StatsModel getStatsModel() {
        if (instance == null) {
            instance = new StatsModel();
        }
        return instance;
    }

    // Calculates the given StatType on the most recent n results
    // N = -1 implies "All Time"
    public Long getReactionTimeStat(StatType statType, int N, Context cxt) {
        ArrayList<Long> times = readReactionTimesFromFile(cxt);
        List<Long> list;
        if (N < 0) {
            list = (List) times;
        } else {
            list = times.subList(0, Math.min(N, times.size()));
        }

        if (list.size() == 0) {
            return 0L;
        }

        Collections.sort(list);
        Long result = -1L;
        switch (statType) {
            case MINIMUM:
                result = list.get(0);
                break;
            case MAXIMUM:
                result = list.get(list.size()-1);
                break;
            case AVERAGE:
                Long sum = 0L;
                for (Long l : list) {
                    sum += l;
                }
                result = sum / list.size();
                break;
            case MEDIAN:
                result = list.get(list.size() / 2);
                break;
        }

        return result;
    }
    public Integer getGameShowStat(Integer players, Integer player, Context cxt) {
        HashMap<Integer, HashMap<Integer, Integer>> buzzes = readGameShowBuzzesFromFile(cxt);
        return buzzes.get(players).get(player);
    }

    public void saveReactionTime(Long elapsed, Context cxt) {
        ArrayList<Long> times = readReactionTimesFromFile(cxt);
        times.add(0, elapsed);
        writeReactionTimesToFile(times, cxt);
    }
    public void saveGameShowBuzz(Integer players, Integer player, Context cxt) {
        HashMap<Integer, HashMap<Integer, Integer>> buzzes = readGameShowBuzzesFromFile(cxt);
        buzzes.get(players).put(player, buzzes.get(players).get(player) + 1);
        writeGameShowBuzzesToFile(buzzes, cxt);
    }

    public void clear(Context cxt) {
        writeReactionTimesToFile(new ArrayList<Long>(), cxt);
        writeGameShowBuzzesToFile(new HashMap<Integer, HashMap<Integer, Integer>>(), cxt);
    }
    public String getEmailBody(Context cxt) {
        String body = "";
        body += "Reaction Timer\tLast 10\tLast 100\tAll Time\n";
        body += String.format("Minimum\t%d\t%d\t%d\n",
                getReactionTimeStat(StatType.MINIMUM, 10, cxt),
                getReactionTimeStat(StatType.MINIMUM, 100, cxt),
                getReactionTimeStat(StatType.MINIMUM, -1, cxt));
        body += String.format("Maximum\t%d\t%d\t%d\n",
                getReactionTimeStat(StatType.MAXIMUM, 10, cxt),
                getReactionTimeStat(StatType.MAXIMUM, 100, cxt),
                getReactionTimeStat(StatType.MAXIMUM, -1, cxt));
        body += String.format("Average\t%d\t%d\t%d\n",
                getReactionTimeStat(StatType.AVERAGE, 10, cxt),
                getReactionTimeStat(StatType.AVERAGE, 100, cxt),
                getReactionTimeStat(StatType.AVERAGE, -1, cxt));
        body += String.format("Median\t%d\t%d\t%d\n",
                getReactionTimeStat(StatType.MEDIAN, 10, cxt),
                getReactionTimeStat(StatType.MEDIAN, 100, cxt),
                getReactionTimeStat(StatType.MEDIAN, -1, cxt));
        body += "\n";

        body += "Game show\t\t# buzzes\n";
        body += "\t2 Players\t3 Players\t4 Players\n";
        body += String.format("Player 1\t%d\t%d\t%d\n",
                getGameShowStat(2, 1, cxt),
                getGameShowStat(3, 1, cxt),
                getGameShowStat(4, 1, cxt));
        body += String.format("Player 2\t%d\t%d\t%d\n",
                getGameShowStat(2, 2, cxt),
                getGameShowStat(3, 2, cxt),
                getGameShowStat(4, 2, cxt));
        body += String.format("Player 3\t\t%d\t%d\n",
                getGameShowStat(3, 3, cxt),
                getGameShowStat(4, 3, cxt));
        body += String.format("Player 4\t\t\t%d\n",
                getGameShowStat(4, 4, cxt));
        body += "\n";
        return body;
    }

    private ArrayList<Long> readReactionTimesFromFile(Context cxt) {
        ArrayList<Long> times = null;
        Gson gson = new Gson();
        try {
            FileInputStream fis = new FileInputStream(getReactionTimesPath(cxt));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            Type type = new TypeToken<ArrayList<Long>>() {}.getType();
            times = gson.fromJson(reader, type);
            fis.close();
        } catch (FileNotFoundException e) {
            Log.e("Stats", String.format("File not found at '{}'", getReactionTimesPath(cxt)));
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (times == null) {
            times = new ArrayList<Long>();
        }
        return times;
    }
    private void writeReactionTimesToFile(ArrayList<Long> times, Context cxt) {
        Gson gson = new Gson();
        try {
            FileOutputStream fos = new FileOutputStream(getReactionTimesPath(cxt));
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            gson.toJson(times, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<Integer, HashMap<Integer, Integer>> readGameShowBuzzesFromFile(Context cxt) {
        HashMap<Integer, HashMap<Integer, Integer>> buzzes = null;
        Gson gson = new Gson();
        try {
            FileInputStream fis = new FileInputStream(getGameShowPath(cxt));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            Type type = new TypeToken<HashMap<Integer, HashMap<Integer, Integer>>>() {}.getType();
            buzzes = gson.fromJson(reader, type);
            fis.close();
        } catch (FileNotFoundException e) {
            Log.e("Stats", String.format("File not found at '{}'", getGameShowPath(cxt)));
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (buzzes == null) {
            buzzes = new HashMap<Integer, HashMap<Integer, Integer>>();
        }
        // Fill with zeros: <[2-4], <[1-4], 0>>
        for (int players=2; players <= 4; players++) {
            for (int playerNum=1; playerNum <= players; playerNum++) {
                if (buzzes.get(players) == null) {
                    buzzes.put(players, new HashMap<Integer, Integer>());
                }
                if (buzzes.get(players).get(playerNum) == null) {
                    buzzes.get(players).put(playerNum, 0);
                }
            }
        }
        return buzzes;
    }
    private void writeGameShowBuzzesToFile(HashMap<Integer, HashMap<Integer, Integer>> buzzes, Context cxt) {
        Gson gson = new Gson();
        try {
            FileOutputStream fos = new FileOutputStream(getGameShowPath(cxt));
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            gson.toJson(buzzes, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getBasePath(Context cxt) {
        return cxt.getFilesDir().getPath().concat("/");
    }
    private String getReactionTimesPath(Context cxt) {
        return getBasePath(cxt).concat(REACTION_TIMES_FILENAME);
    }
    private String getGameShowPath(Context cxt) {
        return getBasePath(cxt).concat(GAME_SHOW_FILENAME);
    }
}

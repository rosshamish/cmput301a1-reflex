package ca.rossanderson.rhanders_reflex;

import android.content.Context;

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

    public void saveReactionTime(Long elapsed, Context cxt) {
        // Read from disk, deserialize
        ArrayList<Long> times = readReactionTimesFromFile(cxt);

        // Prepend to the list
        times.add(0, elapsed);

        // Serialize, write to disk
        writeReactionTimesToFile(times, cxt);
    }

    public void clear(Context cxt) {
        writeReactionTimesToFile(new ArrayList<Long>(), cxt);

        // TODO erase gameshow stats too
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
            System.err.println(String.format("File not found at '{}'", getReactionTimesPath(cxt)));
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

/*
 * Reflex - Reaction Timer and Game Show Buzzer
 * Copyright (C) 2015 Ross Anderson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.rossanderson.rhanders_reflex;

import android.content.Context;
import android.os.Handler;

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
import java.util.Random;

/**
 * Created by ross on 15-09-26.
 */
public abstract class ReactionTimer {
    private static Long MAX_DELAY = 2000L;
    private static Long MIN_DELAY = 50L;

    private Handler handler;
    private Long delay;
    private Long startTime;

    abstract void onStart();
    private Runnable onStartRunnable;
    abstract void onComplete();
    abstract void onKill();

    public ReactionTimer() {
        this.handler = new Handler();
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        final ReactionTimer that = this;
        this.onStartRunnable = new Runnable() {
            @Override
            public void run() {
                that.onComplete();
            }
        };
        handler.postDelayed(this.onStartRunnable, this.randomDelay());
        this.onStart();
    }
    private Long randomDelay() {
        Random r = new Random();
        this.delay = (long)(r.nextInt((int)(MAX_DELAY - MIN_DELAY + 1)) + MIN_DELAY);
        return this.delay;
    }
    private Long getDelay() {
        return this.delay;
    }

    public Long getElapsed() {
        if (this.startTime == null) {
            return null; // uninitialized startTime
        }
        Long elapsed = System.currentTimeMillis() - this.startTime - this.getDelay();
        if (elapsed < 0) {
            return null;
        }
        return elapsed;
    }
    public void kill() {
        handler.removeCallbacks(this.onStartRunnable);
        this.onKill();
    }
    public void restart() {
        this.kill();
        this.start();
    }

    public void saveReactionTime(Long elapsed, Context cxt) {
        StatsModel.getStatsModel().saveReactionTime(elapsed, cxt);
    }
}

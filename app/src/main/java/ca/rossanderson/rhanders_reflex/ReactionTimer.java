package ca.rossanderson.rhanders_reflex;

import android.os.Handler;

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
    public Long randomDelay() {
        Random r = new Random();
        this.delay = (long)(r.nextInt((int)(MAX_DELAY - MIN_DELAY + 1)) + MIN_DELAY);
        return this.delay;
    }
    public Long getDelay() {
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
}

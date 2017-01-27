package com.g_ara.gara.controller;

import com.codename1.ui.Label;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ahmedengu.
 */
public class Countdown extends Label {
    long lastRenderedTime = 0L;
    int remaining;

    public Countdown(int duration) {
        super(((duration > 0) ? duration / 60 : 0) + ":" + duration % 60);
        remaining = duration;
    }


    public Countdown(int duration, CallbackController callback) {
        this(duration);
        if (callback != null) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (callback != null)
                        callback.done();
                }
            }, duration * 1000);
        }
    }

    @Override
    public boolean animate() {
        if (System.currentTimeMillis() / 1000 != lastRenderedTime / 1000 && remaining != -2) {
            aSecond();
            return true;
        }
        return false;
    }

    private void aSecond() {
        if (remaining >= 0) {
            setText(((remaining > 0) ? remaining / 60 : 0) + ":" + remaining % 60);
            remaining--;
            lastRenderedTime = System.currentTimeMillis();
        } else if (remaining == -1) {
            remaining = -2;
            this.getComponentForm().deregisterAnimated(this);
        }
    }

    @Override
    protected void initComponent() {
        super.initComponent();
        this.getComponentForm().registerAnimated(this);
    }

    @Override
    protected void deinitialize() {
        super.deinitialize();
        this.getComponentForm().deregisterAnimated(this);
    }


}



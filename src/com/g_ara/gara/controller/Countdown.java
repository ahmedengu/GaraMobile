package com.g_ara.gara.controller;

import com.codename1.ui.Image;
import com.codename1.ui.Label;

/**
 * Created by ahmedengu.
 */
public class Countdown extends Label {
    long lastRenderedTime = 0L;
    int remaining;
    CallbackController callback;

    public Countdown(int duration) {
        super(((duration > 0) ? duration / 60 : 0) + ":" + duration % 60);
        remaining = duration;
    }

    public Countdown(int duration, String UUID) {
        super(((duration > 0) ? duration / 60 : 0) + ":" + duration % 60, UUID);
        remaining = duration;
    }

    public Countdown(int duration, Image icon, String UUID) {
        super(((duration > 0) ? duration / 60 : 0) + ":" + duration % 60, icon, UUID);
        remaining = duration;
    }

    public Countdown(int duration, CallbackController callback) {
        this(duration);
        this.callback = callback;
    }

    public Countdown(int duration, String UUID, CallbackController callback) {
        this(duration, UUID);
        this.callback = callback;
    }

    public Countdown(int duration, Image icon, String UUID, CallbackController callback) {
        this(duration, icon, UUID);
        this.callback = callback;
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
            if (callback != null)
                callback.done();
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



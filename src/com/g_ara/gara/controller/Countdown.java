package com.g_ara.gara.controller;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import userclasses.StateMachine;

/**
 * Created by ahmedengu.
 */
public class Countdown extends Component {
    long lastRenderedTime = 0L;
    int remaining;
    Label time;
    Container c;
    Callback callback;
    StateMachine stateMachine;

    public Countdown(StateMachine stateMachine, Container c, int duration) {
        remaining = duration;
        time = new Label(((remaining > 0) ? remaining / 60 : 0) + ":" + remaining % 60);
        c.add(time);
        this.c = c;
        this.stateMachine = stateMachine;
    }

    public Countdown(StateMachine stateMachine, Container c, int duration, Callback callback) {
        this(stateMachine, c, duration);
        this.callback = callback;
    }

    @Override
    public boolean animate() {
        if (System.currentTimeMillis() / 1000 != lastRenderedTime / 1000&&remaining>-2) {
            aSecond();
            return true;
        }
        return false;
    }

    private void aSecond() {
        if (remaining >= 0) {
            time.setText(((remaining > 0) ? remaining / 60 : 0) + ":" + remaining % 60);
            remaining--;
            lastRenderedTime = System.currentTimeMillis();
        } else if(remaining==-1) {
            remaining=-2;
            this.getComponentForm().deregisterAnimated(this);
            if (callback != null)
                callback.done(stateMachine);
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



package com.g_ara.gara.controller;

import userclasses.StateMachine;

/**
 * Created by ahmedengu.
 */
public abstract class Callback<T> {
    public abstract void done(StateMachine stateMachine);
}

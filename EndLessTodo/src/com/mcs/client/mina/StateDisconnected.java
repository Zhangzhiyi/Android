package com.mcs.client.mina;

public class StateDisconnected extends StateAdapter {

    public StateDisconnected() {
        super("Disconnected");
    }

    @Override
    public boolean isState(int type) {
        return type == State.DISCONNECTED ? true : false;
    }
}

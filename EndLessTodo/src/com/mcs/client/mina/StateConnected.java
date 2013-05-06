package com.mcs.client.mina;

import com.mcs.framework.message.mina.Auth;

public class StateConnected extends StateAdapter {
    public StateConnected() {
        super("Connected");
    }
    
    @Override
    public boolean isState(int type) {
        return type == State.CONNECTED ? true : false;
    }
    
    public State onSendAuth(MinaClient client, Auth auth) {
    	return new StateAuth();
    }
    
}

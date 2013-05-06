package com.mcs.client.mina;

import com.mcs.framework.message.mina.Data;
import com.mcs.framework.message.mina.Ok;
import com.mcs.framework.message.mina.Error;

public class StateReady extends StateAdapter {
    public StateReady() {
        super("Ready");
    }

    @Override
    public boolean isState(int type) {
        return type == State.READY ? true : false;
    }
    
    @Override
    public State onReceiveOk(MinaClient client, Ok ok) {
    	return this;
    }
    
    @Override
    public State onReceiveError(MinaClient client, Error error) {
    	return this;
    }
    
    @Override
    public State onReceiveData(MinaClient client, Data data) {
    	client.handleData(data);
    	return this;
    }
}

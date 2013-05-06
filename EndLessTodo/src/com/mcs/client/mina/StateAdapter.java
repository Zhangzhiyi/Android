package com.mcs.client.mina;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcs.framework.message.mina.Auth;
import com.mcs.framework.message.mina.Data;
import com.mcs.framework.message.mina.Error;
import com.mcs.framework.message.mina.Ok;

public abstract class StateAdapter implements State {
    protected static final Logger logger = LoggerFactory.getLogger("State");
    
    /** 状态的名字 */
    private String name = null;

    public StateAdapter(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public State onReceiveOk(MinaClient client, Ok ok) {
    	logger.error("Error happened: receive Ok when {} state.", this.getName());
    	return this;
    }
    
    @Override
    public State onReceiveError(MinaClient client, Error error) {
    	logger.error("Error happened: receive error when {} state.", this.getName());
    	return this;
    }
    
    @Override
    public State onReceiveData(MinaClient client, Data data) {
    	logger.error("Error happened: receive data when {} state.", this.getName());
    	return this;
    }
    
    public State onSendAuth(MinaClient client, Auth auth) {
    	logger.error("Error happened: send auth when {} state.", this.getName());
    	return this;
    }
    
    @Override
    public boolean deepEqual(State state) {
        if (state == null || (!state.getClass().equals(this.getClass())) ) {
            return false;
        }
        return true;
    }
}

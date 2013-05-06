package com.mcs.framework.message.mina;

import com.mcs.framework.message.Message;
import com.mcs.framework.message.MsgTypes;

public class Ok extends Message{

    private static final int CMD_TYPE = MsgTypes.OK;
    public static final String PRAM_NONCE = "Nonce";

    @Override
    public int getType() { return CMD_TYPE; }
    
    public Ok(String description){
    	super(description, null, null, null);
    }
    
    public String getDescription() {
    	return this.getKey();
    }
}

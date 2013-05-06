package com.mcs.framework.message.mina;

import com.mcs.framework.message.Message;
import com.mcs.framework.message.MsgTypes;

public class Error extends Message{

    private static final int CMD_TYPE = MsgTypes.ERROR;
    public static final String PRAM_DESCRIPTION = "Description";

    @Override
    public int getType() { return CMD_TYPE; }

    public Error(String description){
        super(description, null, null, null);
    }

    public String getDescription() {
        return this.getKey();
    }
}

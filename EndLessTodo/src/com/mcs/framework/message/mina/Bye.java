package com.mcs.framework.message.mina;

import com.mcs.framework.message.Message;
import com.mcs.framework.message.MsgTypes;

/**
 * 和Ready一样，Bye也只需要说明自己的身份即可
 * @author dbds
 */
public class Bye extends Message {

    private static final int CMD_TYPE = MsgTypes.BYE;

    @Override
    public int getType() { return CMD_TYPE; }

    public Bye(){
    }
    
}

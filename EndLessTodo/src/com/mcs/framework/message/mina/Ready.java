package com.mcs.framework.message.mina;

import com.mcs.framework.message.Message;
import com.mcs.framework.message.MsgTypes;

/**
 * Ready只需要说明srcId即可，不需要其他信息
 * @author dbds
 */
public class Ready extends Message{
    
    private static final int CMD_NAME = MsgTypes.READY;

    @Override
    public int getType() { return CMD_NAME; }
    
    public Ready() {
    }
        
}

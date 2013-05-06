package com.mcs.framework.message.mina;

import com.mcs.framework.message.Message;
import com.mcs.framework.message.MsgTypes;

public class Connect extends Message {

    private static final int CMD_TYPE = MsgTypes.CONNECT;
    public static final String PRAM_SRCID = "SrcId";
    public static final String PRAM_DESTID = "DestId";
    
    public Connect(String srcId, String destId) {
        super(null, null, srcId, destId);
    }

    @Override
    public int getType() {
        return CMD_TYPE;
    }
    
}

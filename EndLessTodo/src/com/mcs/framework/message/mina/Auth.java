package com.mcs.framework.message.mina;

import com.mcs.framework.message.Message;
import com.mcs.framework.message.MsgTypes;

/**
 * 请求鉴权，此时key为用户email地址，content为password，srcId为deviceId
 * @author dbds
 */
public class Auth extends Message {

    private static final int MSG_TYPE = MsgTypes.AUTH;

    public Auth(String id, byte[] cred) {
        super(null, cred, id, null);
    }

    public byte[] getCred() {
        return this.getContent();
    }
    
    public String getId() {
        return this.getSrcId(); // 在Auth里面，id就是srcId
    }

    @Override
    public int getType() {
        return MSG_TYPE;
    }

}

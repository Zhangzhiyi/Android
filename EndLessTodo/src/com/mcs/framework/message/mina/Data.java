package com.mcs.framework.message.mina;

import com.mcs.framework.message.Message;
import com.mcs.framework.message.MsgTypes;

public class Data extends Message {

    private static final int CMD_TYPE = MsgTypes.DATA;

    @Override
    public int getType() { return CMD_TYPE; }
    
    public Data(String key, byte[] content, String srcId, String destId) {
        super(key, content, srcId, destId);
    }
    
    public Data() {}

	public String getKey() {
    	return super.getKey();
    }
	
	public void setKey(String key) {
		super.setKey(key);
	}
    
    public byte[] getContent() {
    	return super.getContent();
    }
    
    public void setContent(byte[] content) {
    	super.setContent(content);
    }
    
    public String getSrcId() {
    	return super.getSrcId();
    }
    
    public void setSrcId(String id) {
    	super.setSrcId(id);
    }
    
    public String getDestId() {
    	return super.getDestId();
    }
    
    public void setDestId(String id) {
    	super.setDestId(id);
    }
}

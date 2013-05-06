package com.mcs.framework.message;

/**
 * 消息的类型
 * @author dbds
 */
public class MsgTypes {
	public static final int NULL			 = 0;
    public static final int OK               = 1;
    public static final int ERROR            = 2;
    public static final int READY            = 3;
    public static final int BYE              = 4;
    public static final int DATA             = 5;
    public static final int CONNECT			 = 6;
    public static final int AUTH             = 7;
    
    public static final String TMP_AUTH	 	 = "Temp";
}

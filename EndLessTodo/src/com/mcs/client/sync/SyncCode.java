package com.mcs.client.sync;

public class SyncCode {
	// 命名规则：[动作|类型]_[名称]
	// 字符串内容一律首字母大写
	// int数值百位为大类，如100系列为同步相关命令，十位为小类，如110系列为同步的方向
	
	// 同步的类型
	public static final int SYNC_NONEED 		= 100;
	public static final int SYNC_FAST 			= 101;
	public static final int SYNC_SLOW			= 102;
	
	// 同步数据交换的方向
	public static final int DIR_CLIENT 			= 110;
	public static final int DIR_SERVER 			= 111;
	public static final int DIR_BOTH   			= 112;
	
	// 当无TimeAnchor的时候采用
	/** 无效的TimeAnchor */
	public static final int INVALID_TIMEANCHOR 	= -1;
	
	// 同步消息中pkg的类型
	public static final String PKG_TIMEANCHOR 	= "TimeAnchor";
	public static final String PKG_SYNCMODE   	= "SyncMode";
	public static final String PKG_SYNCITEM   	= "SyncItem";
	
	// 同步之间交换的消息类型
	/** 同步请求 */
	public static final String MSG_REQUEST 			= "Request";
	/** 同步的模式 */
	public static final String MSG_MODE 			= "Mode";
	/** 改变列表 */
	public static final String MSG_CHANGE_LIST 		= "ChangeList";
	/** 数据请求 */
	public static final String MSG_DATA_REQUEST 	= "DataRequest";
	/** 同步的数据，应当只包含必须传输的数据 */
	public static final String MSG_DATA 			= "Data";
	/** 确认 */
	public static final String MSG_CONFIRM 			= "Confirm";
	/** 错误 */
	public static final String MSG_ERROR			= "Error";
	
	// 消息交换的时候附带的动作，比如指示data发送完毕，或者还有其他数据扥等
	/** 指示本包之后就没有数据了 */
	public static final String ACTION_DATA_COMPLETE = "DataComplete";
}

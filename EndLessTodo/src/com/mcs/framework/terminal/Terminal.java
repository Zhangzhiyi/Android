package com.mcs.framework.terminal;

import com.mcs.framework.message.mina.Data;

public interface Terminal {
	/** 接收到消息 */
	void receiveMessage(Data msg) throws TerminalWriteFailedException;
}

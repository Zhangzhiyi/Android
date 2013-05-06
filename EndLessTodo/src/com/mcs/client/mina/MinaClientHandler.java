package com.mcs.client.mina;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcs.framework.message.Message;

public class MinaClientHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger("MinaClientHandler");
	
	private MinaClient client;

	public MinaClientHandler (MinaClient client) {
		this.client = client;
	}

    @Override
    public void sessionCreated(IoSession session) {
    	logger.info("Session created.");
        SessionUtil.initialize(session);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
    	logger.warn("EXCEPTION, please implement {}.exceptionCaught() for proper handling: {}",
                    getClass().getName(), cause);
    }

    @Override
    public void messageReceived(IoSession session, Object msg) throws MinaClientException {
        if (!(msg instanceof Message)) {
            throw new MinaClientException("Received message is not a valid Message");
        }
        client.msgFromServer((Message) msg);
    }
}

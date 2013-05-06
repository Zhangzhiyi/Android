package com.mcs.client.mina;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoConnector;
import org.apache.mina.common.IoConnectorConfig;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.PooledByteBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcs.client.android.Env;
import com.mcs.client.mina.filter.codec.ClientProtocolCodecFactory;
import com.mcs.client.sync.DataType;
import com.mcs.framework.message.Message;
import com.mcs.framework.message.mina.Auth;
import com.mcs.framework.message.mina.Bye;
import com.mcs.framework.message.mina.Data;
import com.mcs.framework.message.mina.Error;
import com.mcs.framework.message.mina.Ok;
import com.mcs.framework.message.mina.Ready;
import com.mcs.framework.terminal.TerminalWriteFailedException;

public class MinaClient {
	
	private static final Logger logger = LoggerFactory.getLogger("MinaClient");
	
	// mina相关
    private InetAddress hostAddress;
    private int port;
    private IoConnector connector;
    private IoConnectorConfig cfg;
    private IoSession session =  null;
    private MinaClientHandler clientHandler = null;
    
    // 客户端的状态机
    private State clientState;
    
    // 发送和接受分别两套锁
    private ReentrantLock receiveLock = new ReentrantLock();
    private ReentrantLock sendLock = new ReentrantLock();

    public MinaClient(InetAddress hostAddress, int port) {
        this.hostAddress = hostAddress;
        this.port 		 = port;
        this.clientState = new StateConnected();

        ByteBuffer.setUseDirectBuffers(false);
        ByteBuffer.setAllocator(new PooledByteBufferAllocator());

        connector = new SocketConnector();

        cfg = new SocketConnectorConfig();
        cfg.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new ClientProtocolCodecFactory()));
        //cfg.getFilterChain().addLast("logger", new LoggingFilter());

        cfg.setConnectTimeout(30);
    }

    /** 连接到服务器 */
    public boolean connect() {
    	setClientState(new StateConnected());
    	logger.debug("MinaClient: {}", this);
        logger.debug("Connecting to: {}/{}", this.hostAddress, this.port);
        clientHandler = new MinaClientHandler(this);
        InetSocketAddress address = new InetSocketAddress(this.hostAddress, this.port);
        ConnectFuture future = connector.connect(address, clientHandler, cfg);
        future.join();
        if (!future.isConnected()) {
            logger.error("Unable to Connect");
            return false;
        }
        session = future.getSession();
        logger.info("Connected");
        return true;
    }
    
    /** 关闭连接 */
    public void quit() {
    	logger.info("Close connection.");
    	stopHeartBeat();
    	write(new Bye());
        
    	if (session != null) {
            session.close();
        }
        setClientState(new StateDisconnected());
    }
    
    /** 处理收到的数据 */
    public void handleData(Data data) {
    	//logger.debug("Handle data from {}.", data.getSrcId());
    	String dataType = data.getKey();
    	if(dataType.equals(DataType.SYNC_TODO)) {
    		try {
				Env.getInstance().getSyncTer().receiveMessage(data);
			} catch (TerminalWriteFailedException e) {
				e.printStackTrace();
			}
    	}
    }
    
    /** 实际将消息发送 */
    private void write(Message msg) {
        if (session == null) {
            logger.error("Unable to send message: session non created.");
            return;
        }
        session.write(msg);
    }
    
    public void send(Message msg) {
    	// 这里保证sendAuth会被调用，同时还保证send接口的简洁
    	if(msg instanceof Auth) {
    		sendAuth((Auth) msg);
    	}
    	else write(msg);
    }
    
    private void sendAuth(Auth auth) {
    	sendLock.lock();
    	logger.info("Send auth: {}.", auth.getId());
    	State nextState = clientState.onSendAuth(this, auth);
    	write(auth);
    	setClientState(nextState);
    	sendLock.unlock();
    }
    
    /** 接收从服务器来的消息 */
    public void msgFromServer(Message msg) {
        if(msg instanceof Data)  receiveData((Data)msg);
        else if (msg instanceof Ok) receiveOk((Ok)msg);
        else if (msg instanceof Error) receiveError((Error)msg);
    }
    
    /** 收到消息 */
    public void receiveData(Data data) {
    	receiveLock.lock();
    	//logger.info("Receive data.");
    	State nextState = clientState.onReceiveData(this, data);
    	setClientState(nextState);
    	receiveLock.unlock();
    }
    
    public void receiveOk(Ok ok) {
    	receiveLock.lock();
    	logger.info("Receive ok: {}.", ok.getDescription());
    	State nextState = clientState.onReceiveOk(this, ok);
    	setClientState(nextState);
    	receiveLock.unlock();
    }
    
    public void receiveError(Error error) {
    	receiveLock.lock();
    	logger.info("Receive error: {}.", error.getDescription());
    	State nextState = clientState.onReceiveError(this, error);
    	setClientState(nextState);
    	receiveLock.unlock();
    }
    
    private void setClientState(State state) {
        if (this.clientState.deepEqual(state)) {
            return;
        }
        // FIXME 加入对状态转换的检查
        logger.debug("Changing status from {} to {}.", this.clientState.getName(), state.getName());
        this.clientState = state;
    }
    
    private Timer timer = null;
    // TODO 设置HeartBeat的时间间隔
    private long heartBeatPeriod = 10; // 10秒
    
    public void startHeartBeat() {
    	logger.info("Start heartbeat.");
    	timer = new Timer();
    	timer.scheduleAtFixedRate(new ReadyHeartBeat(), 0, heartBeatPeriod*1000);
    }
    
    public void stopHeartBeat() {
    	logger.info("Stop heartbeat.");
    	timer.cancel();
    }
    
    // 
    private class ReadyHeartBeat extends TimerTask{
		@Override
		public void run() {
			write(new Ready());
		}
	}
}

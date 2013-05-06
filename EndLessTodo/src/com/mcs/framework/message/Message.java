package com.mcs.framework.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcs.client.android.Configeration;
import com.mcs.framework.message.mina.Auth;
import com.mcs.framework.message.mina.Bye;
import com.mcs.framework.message.mina.Connect;
import com.mcs.framework.message.mina.Data;
import com.mcs.framework.message.mina.Error;
import com.mcs.framework.message.mina.Ok;
import com.mcs.framework.message.mina.Ready;

/**
 * 节点之间发送的消息
 * 应该说一个消息包含key, content, srcId, destId就算是完整的了
 * @author dbds
 */
public abstract class Message {
    private static final Logger logger = LoggerFactory.getLogger("Message");
    private static final String ENCODING = Configeration.getInstance().getEncoding();
	
	/**
	 * 这个是消息到达服务器的时间，应由服务器设置
	 */
	private long timeStamp = 0;
	
	/**
	 * 由服务器来设置消息到达服务器的时间，作为消息的时间戳
	 * @param time
	 */
	public void setTimeStamp(long time) {
		this.timeStamp = time;
	}
	
	public long getTimeStamp() {
		return this.timeStamp;
	}
	
    public Message(){
    }
	
	public Message(String key, byte[] content, String srcId, String destId) {
		this.key = key;
		this.content = content;
		this.srcId = srcId;
		this.destId = destId;
	}
	
	/**
	 * 消息的key
	 */
	private String key;
	
	/**
	 * 消息的主体
	 */
	private byte[] content;
	
	/**
	 * 发送消息的节点的id
	 */
	private String srcId;

	/**
	 * 目标节点的id
	 */
	private String destId;
	
	protected String getSrcId() {
		return srcId;
	}

	protected void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	protected String getDestId() {
		return destId;
	}

	protected void setDestId(String destId) {
		this.destId = destId;
	}

	protected String getKey() {
		return key;
	}

	protected void setKey(String key) {
		this.key = key;
	}

	protected byte[] getContent() {
		return content;
	}

	protected void setContent(byte[] content) {
		this.content = content;
	}

    /**
     * 返回消息的类型
     */
    public abstract int getType();
    
    public static byte[] getByte(Message msg){
        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(msgBytes);

        try {
            // 首先写入消息的类型
            dos.writeInt(msg.getType());
            
            // 写入消息的Key
            byte[] b = null;
            if (null != msg.getKey()) {
                b = msg.getKey().getBytes(ENCODING);
                // 如果key存在，则按顺序写入长度，再写入key
                dos.writeInt(b.length);
                dos.write(b);
            }else dos.writeInt(0); // key不存在，长度写入0

            // 写入Content
            b = msg.getContent();
            if (null != b) {
                dos.writeInt(b.length);
                dos.write(b);
            }else dos.writeInt(0);

            if (null != msg.getSrcId()) {
                b = msg.getSrcId().getBytes(ENCODING);
                dos.writeInt(b.length);
                dos.write(b);
            }else dos.writeInt(0);

            if (null != msg.getDestId()) {
                b = msg.getDestId().getBytes(ENCODING);
                dos.writeInt(b.length);
                dos.write(b);
            }else dos.writeInt(0);
            
            // 用完要关闭
            dos.close();
        } catch (IOException ex) {
            logger.error("Convert message error!");
        }
        
        return msgBytes.toByteArray();
    }

    public static Message getMsgFromBytes(byte[] in) {
        DataInputStream input = new DataInputStream(new ByteArrayInputStream(in));
        int type = 0, length = 0, offset = 0;
        // Message的四个部分
        byte[] keyB = null, content = null, srcIdB = null, destIdB = null;
        String key = null, srcId = null, destId = null;
        try {
            // 读取type
            type = input.readInt();
            
            // 载入Msg的四部分值
            length = input.readInt();
            if (length != 0) {
                keyB = new byte[length];
                input.read(keyB, offset, length);
                key = new String(keyB);
            }

            length = input.readInt();
            if (length != 0) {
                content = new byte[length];
                input.read(content, offset, length);
            }

            length = input.readInt();
            if (length != 0) {
                srcIdB = new byte[length];
                input.read(srcIdB, offset, length);
                srcId = new String(srcIdB);
            }

            length = input.readInt();
            if (length != 0) {
                destIdB = new byte[length];
                input.read(destIdB, offset, length);
                destId = new String(destIdB);
            }

            input.close();
        } catch (IOException ex) {
            logger.error("Parse message error!");
        }
        
        // 根据不同的类型，对消息进行赋值
        Message msg = null;
        switch (type) {
            case MsgTypes.BYE:
                msg = new Bye();
                break;
            case MsgTypes.DATA:
                msg = new Data(key, content, srcId, destId);
                break;
            case MsgTypes.ERROR:
				msg = new Error(key);
				break;
            case MsgTypes.OK:
                msg = new Ok(key);
                break;
            case MsgTypes.READY:
                msg = new Ready();
                break;
            case MsgTypes.CONNECT:
            	msg = new Connect(srcId, destId);
            	break;
            case MsgTypes.AUTH:
            	msg = new Auth(srcId, content);
            	break;	
        }

        return msg;
    }
}

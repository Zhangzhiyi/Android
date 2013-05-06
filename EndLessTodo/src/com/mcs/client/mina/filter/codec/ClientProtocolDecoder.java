package com.mcs.client.mina.filter.codec;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.mcs.framework.message.Message;

/**
 * 与客户端不同的就是Server端的Protocol会设置消息的时间。
 * @author dbds
 */
public class ClientProtocolDecoder extends CumulativeProtocolDecoder{

    @Override
    protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
    	// 记住开始的位置
    	int start = in.position();
    	int end = in.limit();
    	int length = in.getInt();

    	if(length <= (end - start)){
    		byte[] msgBytes = new byte[length];
    		if(length > 0) in.get(msgBytes, 0, length);
    		Message msg = Message.getMsgFromBytes(msgBytes);
    		if(null != msg) {
    			out.write(msg);
    			return true;
    		}
    	}
    	in.position(start);
		return false;
    }

}

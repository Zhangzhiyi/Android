package com.mcs.client.mina.filter.codec;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.mcs.framework.message.Message;

/**
 * 与客户端不同的就是Server端的Protocol会设置消息的时间。
 * @author dbds
 */
public class ClientProtocolEncoder extends ProtocolEncoderAdapter{

    @Override
    public void encode(IoSession session, Object object, ProtocolEncoderOutput out) throws Exception {
        if (object instanceof Message) {
        	Message msg = (Message)object;
            byte[] b = Message.getByte(msg);
            // 分配buffer，长度上要预留一个int来保存消息长度
            ByteBuffer buffer = ByteBuffer.allocate(b.length + 4, false);
            buffer.putInt(b.length);
            buffer.put(b);
            buffer.flip();
            out.write(buffer);
        }
    }
    
}

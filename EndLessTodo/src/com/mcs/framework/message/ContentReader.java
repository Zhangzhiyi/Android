package com.mcs.framework.message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * 读取消息Content的工具类
 * @author dbds
 */
public class ContentReader {
    private byte[] content = null;
	private String type;
	private String action;
	
    /** Msg中的Pkg数 */
    private int count = -1;
    /** 保存每个Pkg的offset */
    private long[] offsets;
    /** 用于读数据的stream */
    private DataInputStream input = null;
    
    public ContentReader(byte[] content) {
        this.content = content;
        if(null != content) {
            input = new DataInputStream(new ByteArrayInputStream(this.content));
            try {
            	// 读取type
            	int length = input.readInt();
            	type = readString(input, length);
            	// 读取action
            	length = input.readInt();
            	action = readString(input, length);
            	// 读取pkg数
                count = input.readInt();
                if(count != 0) {
                    // offset以long保存
                    offsets = new long[count];
                    for (int i = 0; i < count; i++) {
                        offsets[i] = input.readLong();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private String readString(DataInputStream input, int length) {
    	if(length == 0) return null;
		try {
			byte[] value = new byte[length];
			if (input.read(value, 0, length) == length)
				return new String(value);
		} catch (IOException e) {}
    	return null;
    }
	
	/** 返回index指定的Pkg的Head */
	private byte[] getPkgHead(int index){
		try {
		    // 将input的指针放到要读取的那个Pkg的位置上
	        long offset = offsets[index];
	        input.reset();
	        input.skip(offset);
		    // 读取head的长度
			int length = input.readInt();
			if(length != 0) {
			    byte[] head = new byte[length];
			    if(input.read(head, 0, length) == length)
			    	return head;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 返回index指定的pkg的类型 */
	public String getPkgType(int index) {
		byte[] value = getPkgHead(index);
		if(value != null)
			return new String(value);
		return null;
	}
	
	/** 返回Content中的Value部分 */
	public byte[] getPkgValue(int index) {
		try {
	        // 将input的指针放到要读取的那个Pkg的位置上
            long offset = offsets[index];
            input.reset();
            input.skip(offset);
            // 跳过Head
		    // 注意readInt()的时候，int这4个byte就已经被跳过去了
			int length = input.readInt(); 
			input.skip(length);
			// 获得CONTENT的长度
			length = input.readInt(); 
			if (length != 0) {
				byte[] body = new byte[length];
				input.read(body, 0, length);
				return body;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 返回包中的Pkg数 */
	public int getCount() {
	    return count;
	}
	
	/** 返回整个包的类型 */
	public String getType() {
		return type;
	}
	
	/** 返回整个包的Action */
	public String getAction() {
		return action;
	}
}

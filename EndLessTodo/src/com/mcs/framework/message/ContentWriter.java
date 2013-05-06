package com.mcs.framework.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.mcs.framework.utils.Bytes;

public class ContentWriter {
	
	/** 本数据包的名字 */
	private String name;
	/** 本数据包做什么用的 */
	private String action;
	
	/** 缓存用户put的pkg */
	private ArrayList<byte[]> pkgs;
	
	/**
	 * ContentWriter对象用来构建Message中的Content
	 * @param name content包名
	 * @param action 对本包执行的action
	 */
	public ContentWriter(String name, String action) {
		this.name = name;
		this.action = action;
	    pkgs = new ArrayList<byte[]>();
	}
	
	/** 向Msg添加一个Pkg */
	public void putPkg(String type, byte[] value) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(out);
		try {
		    // 先构造Head
		    if(null != type) {
		       byte[] body = Bytes.fromString(type);
		       dos.writeInt(body.length);
		       if(body.length != 0) {
		    	   dos.write(body, 0, body.length);
		       }
		    }else dos.write(0);
		    // pkgHead中是不包含action的
		    
		    // 写入value
			if(null != value) {
				dos.writeInt(value.length);
				dos.write(value);
			}else dos.writeInt(0);
			
			dos.flush();
			// 加入到pkg缓存中
			pkgs.add(out.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 返回由之前put进来的pkg组成的content */
	public byte[] getContent() {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        long offset = 0;
        try {
        	// 写入整个包的type
        	if(null != name) {
        		byte[] bName = Bytes.fromString(name);
        		// 这里长度用int，用不着long
        		dos.writeInt(bName.length);
        		dos.write(bName);
        		offset += 4 + bName.length;
        	}else {
        		dos.writeInt(0);
        		offset += 4;
        	}
        	// 写入整个包的action
        	if(null != action) {
        		byte[] bAction = Bytes.fromString(action);
        		dos.writeInt(bAction.length);
        		dos.write(bAction);
        		offset += 4 + bAction.length;
        	}else {
        		dos.writeInt(0);
        		offset += 4;
        	}
        	        	
            // 写入pkg的count
            dos.writeInt(pkgs.size());
            offset += 4; // int为4字节大
            
            if(pkgs.size() != 0) {
                // 几个Offset值也有长度 (为什么*8？) 因为offsets是long型的
                offset += pkgs.size() * 8;
                // 计算各个offset
                long[] offsets = new long[pkgs.size()];
                // 写入Offset值
                for(int i = 0; i < offsets.length; i++) {
                    offsets[i] = offset;
                    offset += pkgs.get(i).length;
                }
                // 写入offset
                for(int i = 0; i < offsets.length; i++) {
                    dos.writeLong(offsets[i]);
                }
                // 写入pkg
                for(int i = 0; i < pkgs.size(); i++) {
                    dos.write(pkgs.get(i));
                }
            }
         
            dos.flush();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
	}
}

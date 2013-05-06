package com.mcs.framework.utils;

import java.io.UnsupportedEncodingException;

import com.mcs.client.android.Configeration;

/**
 * 转换各种数据到符合Cassandra要求的byte[]
 * @author dbds
 */
public class Bytes {
	
	private static final String ENCODING = Configeration.getInstance().getEncoding();
	
	//--------------------------Long------------------------
	
	public static byte[] fromLong(long value) {
		byte[] buf = new byte[8];
		// DataOutputStream的算法，Big-Endian
        buf[0] = (byte)(value >>> 56);
        buf[1] = (byte)(value >>> 48);
        buf[2] = (byte)(value >>> 40);
        buf[3] = (byte)(value >>> 32);
        buf[4] = (byte)(value >>> 24);
        buf[5] = (byte)(value >>> 16);
        buf[6] = (byte)(value >>>  8);
        buf[7] = (byte)(value >>>  0);
        return buf;
	}
	
	public static long toLong(byte[] in){
		return (((long)in[0] << 56) +
                ((long)(in[1] & 255) << 48) +
        		((long)(in[2] & 255) << 40) +
                ((long)(in[3] & 255) << 32) +
                ((long)(in[4] & 255) << 24) +
                ((in[5] & 255) << 16) +
                ((in[6] & 255) <<  8) +
                ((in[7] & 255) <<  0));
	}
	
	//----------------------------String----------------------------
	
	public static byte[] fromString(String value) {
			try {
				return value.getBytes(ENCODING);
			} catch (UnsupportedEncodingException e) {}
			return null;
	}
	
	//-----------------------------Char-----------------------------
	public static byte[] fromChar(char c) {
		byte[] buf = new byte[2];
		buf[0] = (byte)((c >>> 8) & 0xFF);
		buf[1] = (byte)((c >>> 0) & 0xFF);
		return buf;
	}
	
	public static char toChar(byte[] value) {
        int ch1 = value[0];
        int ch2 = value[1];
        return (char)((ch1 << 8) + (ch2 << 0));
	}
	
	public static boolean equals(byte[] b1, byte[] b2) {
		if(b1.length == b2.length) {
			for(int i = 0; i < b1.length; i++) {
				if(b1[i] != b2[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
}

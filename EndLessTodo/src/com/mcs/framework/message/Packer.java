package com.mcs.framework.message;

/**
 * 如果需要被封装到Message的Content中，数据类就必须实现这个接口
 */
public interface Packer {
	
	/** 换行符 */
	static final String NL = "\r\n";
	
	// TODO 找个比较不容易出现的字符
	/** 字符串中分割用的分隔符 */
	static final String SEP = ":";
	
	/**
	 * 将自己打包成一个byte[]
	 */
	byte[] pack();
	
	/**
	 * 解析byte[]中的内容，并赋值到本对象中
	 */
	void unpack(byte[] value);
}

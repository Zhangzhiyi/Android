package com.xiawenquan.test.utils;

import java.util.regex.Pattern;

public class Utils {
	
	/***
	 *获得汉语拼音首字母 
	 *不是字母返回#
	 *是字母返回字母
	 * */ 
	public static String  getAlpha(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}
	
}

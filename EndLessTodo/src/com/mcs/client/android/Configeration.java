package com.mcs.client.android;

public class Configeration {
	private static class ConfHolder {
		public static Configeration conf = new Configeration();
	}
	
	public static Configeration getInstance() {
		return ConfHolder.conf;
	}
	
	public String getEncoding() {
		return "utf-8";
	}
}

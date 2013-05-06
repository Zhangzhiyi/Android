package com.et.Activity;

import com.et.service.RemoteCallbackService;

import android.os.Handler;
import android.os.Process;

public class TestCallbackThread implements Runnable {

	private Handler handler;
	public TestCallbackThread(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		/**打印进程号,判断与服务或activity是否在同一进程**/
		int pid = Process.myPid();		
		handler.sendEmptyMessage(RemoteCallbackService.THREAD_MSG);

	}

}

package com.et.Activity;

import com.et.service.Constant;
import com.et.service.RemoteService;

import android.os.Handler;
import android.os.Process;
import android.util.Log;

public class TestThread implements Runnable {
	private Handler handler;
	private int n = 0;
	public TestThread(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		/**打印进程号,判断与服务或activity是否在同一进程**/
		int pid = Process.myPid();		
		Log.i("Thread Process ID", String.valueOf(pid));
		Log.i("RemoteThread value", String.valueOf(RemoteService.value));
		Log.i("RemoteThread flag", String.valueOf(Constant.flag));
		/**发送消息修改value的值**/
		handler.sendEmptyMessage(10);
		/**第一次执行这个线程这里打印的value还是没有在handler里面修改后的值,有个时间差**/
		/**解决方法：建个无限循环来等待完成**/
		while(!Constant.flag){	
			n++;
			Log.i("Thread wait", String.valueOf(n));
		}
		Log.i("RemoteThread value", String.valueOf(RemoteService.value));
	}

}

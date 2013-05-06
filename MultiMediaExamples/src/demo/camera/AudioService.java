package demo.camera;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.MediaController.MediaPlayerControl;

/**
 * 为了可以使得在后台播放音乐，我们需要Service
 * Service就是用来在后台完成一些不需要和用户交互的动作
 * @author Administrator
 *
 */
public class AudioService extends Service implements MediaPlayer.OnCompletionListener{
	
	MediaPlayer player;
	
	private final IBinder binder = new AudioBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return binder;
	}

	/**
	 * 当Audio播放完的时候触发该动作
	 */
	@Override
	public void onCompletion(MediaPlayer player) {
		// TODO Auto-generated method stub
		stopSelf();//结束了，则结束Service
	}
	
	//在这里我们需要实例化MediaPlayer对象
	public void onCreate(){
		super.onCreate();
		//我们从raw文件夹中获取一个应用自带的mp3文件
		player = MediaPlayer.create(this, R.raw.nami);
		player.setOnCompletionListener(this);
	}
	
	/**
	 * 该方法在SDK2.0才开始有的，替代原来的onStart方法
	 */
	public int onStartCommand(Intent intent, int flags, int startId){
		if(!player.isPlaying()){
			player.start();
		}
		return START_STICKY;
	}
	
	public void onDestroy(){
		//super.onDestroy();
		if(player.isPlaying()){
			player.stop();
		}
		player.release();
	}
	
	//为了和Activity交互，我们需要定义一个Binder对象
	class AudioBinder extends Binder{
		
		//返回Service对象
		AudioService getService(){
			return AudioService.this;
		}
	}
	
	//后退播放进度
	public void haveFun(){
		if(player.isPlaying() && player.getCurrentPosition()>1000){
			player.seekTo(player.getCurrentPosition()-1000);
		}
	}

}

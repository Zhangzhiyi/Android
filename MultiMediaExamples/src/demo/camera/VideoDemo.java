package demo.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;
/**
 * 本实例主要介绍Android中Video相关内容
 * 首先我们可以通过调用系统自带的MediaPlayer来完成播放
 * 指定Action为ACTION_VIEW,Data为文件URI，类型为MIME类型
 * 
 * 第二种方式：使用VideoView和MediaController来实现播放
 * 
 * 第三种方式，使用MediaPlayer类结合SurfaceView来完成，这种方式灵活性最高
 * 
 * 注意了，在模拟器上运行时，会出现播放时卡在第一帧的问题，这是模拟器的问题，放在真机上就正常了。
 * @author Administrator
 *
 */
public class VideoDemo extends Activity {
	
	//private VideoView videoView;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.video);
		
		//videoView = (VideoView)this.findViewById(R.id.video_view);
	}
	
	public void onClick(View v){
		int id = v.getId();
		Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/Test_Movie.m4v");
		switch(id){
		case R.id.btn1:
			
			//调用系统自带的播放器
			Intent intent = new Intent(Intent.ACTION_VIEW);

			Log.v("URI:::::::::", uri.toString());
			intent.setDataAndType(uri, "video/mp4");
			startActivity(intent);
			break;
		case R.id.btn2:
			//使用VideoView来实现，原来直接在这里实现不行，还以为不行，结果是模拟器的问题，这样也不行，卡在第一帧
//			videoView.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Test_Movie.m4v");
//			videoView.start();
			Intent intent2 = new Intent();
			intent2.setClass(this, VideoViewDemo.class);
			startActivity(intent2);
			break;
		case R.id.btn3:
			//使用MediaPlayer来完成
			Intent intent3 = new Intent();
			intent3.setClass(this, VideoSurfaceDemo.class);
			startActivity(intent3);
			break;
			
		case R.id.btn4:
			//检索多媒体库
			Intent intent4 = new Intent();
			intent4.setClass(this, AdvancedVideoDemo.class);
			startActivity(intent4);
			break;
		case R.id.btn5:
			Intent intent5 = new Intent();
			intent5.setClass(this, InternetVideoDemo.class);
			startActivity(intent5);
			break;
		case R.id.btn6:
			Intent intent6 = new Intent();
			intent6.setClass(this, VideoCaptureDemo.class);
			startActivity(intent6);
			break;
		}
	}

}

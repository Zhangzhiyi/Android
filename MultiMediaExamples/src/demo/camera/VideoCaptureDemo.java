package demo.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.VideoView;
/**
 * 本实例介绍怎样利用Android自带的Camera来录制视频
 * 
 * 指定Action为MediaStore.ACTION_VIDEO_CAPTURE
 * @author Administrator
 *
 */
public class VideoCaptureDemo extends Activity {
	
	private VideoView videoView;
	
	private Uri videoUri;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_capture);
		
		videoView = (VideoView)this.findViewById(R.id.video_view);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			videoUri = data.getData();
		}
	}
	
	public void onClick(View v){
		int id = v.getId();
		
		if(id == R.id.btn_capture){
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			startActivityForResult(intent, 1);
		}else if(id == R.id.btn_play){
			videoView.setVideoURI(videoUri);
			videoView.start();
		}
	}
}

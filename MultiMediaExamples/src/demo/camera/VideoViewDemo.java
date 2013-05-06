package demo.camera;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewDemo extends Activity {
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.video_view);
		
		Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/Test_Movie.m4v");
		VideoView videoView = (VideoView)this.findViewById(R.id.video_view);
		videoView.setMediaController(new MediaController(this));
		videoView.setVideoURI(uri);
		//videoView.start();
		videoView.requestFocus();
	}

}

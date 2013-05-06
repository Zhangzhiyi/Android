package demo.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

/**
 * 被实例演示如何调用Android自带的应用来完成Audio的录入
 * 其实很简单，我们需要指定一个MediaStore.Audio.Media.RECORD_SOUND_ACTION的Action来启动就可以
 * 返回的Data数据就是我们录制的音频的URI了
 * 
 * 通过上面这种方式，灵活性不够高，我们可以利用MediaRecorder类来实现自己的音频录制程序
 * MediaRecorder既可以用来录制音频，也可以用来录制视频
 * 创建了一个MediaRecorder实例后，需要调用setAudioSource和setAudioEncoder来初始化
 * 通常情况下，在准备录制前，我们还需要调用setOutputFormat()方法来决定使用的音频格式，同时调用
 * setOutputFile()来指定存放录制内容的文件
 * 
 * 这几个方法的调用顺序是：setAudioSource,setOutputFormat,setAudioEncoder,setOutputFile
 * 
 * 
 * 
 * @author Administrator
 *
 */
public class AudioRecordDemo extends Activity {
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_record);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		//super.onActivityResult(requestCode, resultCode, data);
		//这里我们就可以获取到刚刚录制的音频的Uri，可以进行播放等操作，这里显示返回的Uri
		if(resultCode == RESULT_OK){
			Uri audioPath = data.getData();
			Toast.makeText(this, audioPath.toString(), Toast.LENGTH_LONG).show();
		}

	}
	
	public void onClick(View v){
		int id = v.getId();
		switch(id){
		case R.id.btn1:
			//调用Android自带的音频录制应用
			Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
			startActivityForResult(intent, 0);
			break;
		case R.id.btn2:
			//通过MediaRecorder类来实现自己的音频录制程序
			Intent intent2 = new Intent();
			intent2.setClass(this, MyAudioRecord.class);
			startActivityForResult(intent2, 1);
			break;
		case R.id.btn3:
			//通过AudioRecord类实现自己的音频录制程序
			Intent intent3 = new Intent();
			intent3.setClass(this, MyAudioRecord2.class);
			startActivity(intent3);
			break;
		}
	}

}

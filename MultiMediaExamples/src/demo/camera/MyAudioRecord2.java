package demo.camera;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.hardware.Camera.AutoFocusCallback;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 该实例中，我们使用AudioRecord类来完成我们的音频录制程序
 * AudioRecord类，我们可以使用三种不同的read方法来完成录制工作，
 * 每种方法都有其实用的场合
 * 一、实例化一个AudioRecord类我们需要传入几种参数
 * 1、AudioSource：这里可以是MediaRecorder.AudioSource.MIC
 * 2、SampleRateInHz:录制频率，可以为8000hz或者11025hz等，不同的硬件设备这个值不同
 * 3、ChannelConfig:录制通道，可以为AudioFormat.CHANNEL_CONFIGURATION_MONO和AudioFormat.CHANNEL_CONFIGURATION_STEREO
 * 4、AudioFormat:录制编码格式，可以为AudioFormat.ENCODING_16BIT和8BIT,其中16BIT的仿真性比8BIT好，但是需要消耗更多的电量和存储空间
 * 5、BufferSize:录制缓冲大小：可以通过getMinBufferSize来获取
 * 这样我们就可以实例化一个AudioRecord对象了
 * 二、创建一个文件，用于保存录制的内容
 * 同上篇
 * 三、打开一个输出流，指向创建的文件
 * DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))
 * 四、现在就可以开始录制了，我们需要创建一个字节数组来存储从AudioRecorder中返回的音频数据，但是
 * 注意，我们定义的数组要小于定义AudioRecord时指定的那个BufferSize
 * short[]buffer = new short[BufferSize/4];
 * startRecording();
 * 然后一个循环，调用AudioRecord的read方法实现读取
 * 另外使用MediaPlayer是无法播放使用AudioRecord录制的音频的，为了实现播放，我们需要
 * 使用AudioTrack类来实现
 * AudioTrack类允许我们播放原始的音频数据
 * 
 * 
 * 一、实例化一个AudioTrack同样要传入几个参数
 * 1、StreamType:在AudioManager中有几个常量，其中一个是STREAM_MUSIC;
 * 2、SampleRateInHz：最好和AudioRecord使用的是同一个值
 * 3、ChannelConfig：同上
 * 4、AudioFormat：同上
 * 5、BufferSize：通过AudioTrack的静态方法getMinBufferSize来获取
 * 6、Mode：可以是AudioTrack.MODE_STREAM和MODE_STATIC，关于这两种不同之处，可以查阅文档
 * 二、打开一个输入流，指向刚刚录制内容保存的文件，然后开始播放，边读取边播放
 * 
 * 实现时，音频的录制和播放分别使用两个AsyncTask来完成 
 */
public class MyAudioRecord2 extends Activity{
	
	private TextView stateView;
	
	private Button btnStart,btnStop,btnPlay,btnFinish;
	
	private RecordTask recorder;
	private PlayTask player;
	
	private File audioFile;
	
	private boolean isRecording=true, isPlaying=false; //标记
	
	private int frequence = 8000; //录制频率，单位hz.这里的值注意了，写的不好，可能实例化AudioRecord对象的时候，会出错。我开始写成11025就不行。这取决于硬件设备
	private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_audio_record);
		
		stateView = (TextView)this.findViewById(R.id.view_state);
		stateView.setText("准备开始");
		btnStart = (Button)this.findViewById(R.id.btn_start);
		btnStop = (Button)this.findViewById(R.id.btn_stop);
		btnPlay = (Button)this.findViewById(R.id.btn_play);
		btnFinish = (Button)this.findViewById(R.id.btn_finish);
		btnFinish.setText("停止播放");
		btnStop.setEnabled(false);
		btnPlay.setEnabled(false);
		btnFinish.setEnabled(false);
		
		//在这里我们创建一个文件，用于保存录制内容
		File fpath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/data/files/");
		fpath.mkdirs();//创建文件夹
		try {
			//创建临时文件,注意这里的格式为.pcm
			audioFile = File.createTempFile("recording", ".pcm", fpath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	public void onClick(View v){
		int id = v.getId();
		switch(id){
		case R.id.btn_start:
			//开始录制
			
			//这里启动录制任务
			recorder = new RecordTask();
			recorder.execute();
			
			break;
		case R.id.btn_stop:
			//停止录制
			this.isRecording = false;
			//更新状态
			//在录制完成时设置，在RecordTask的onPostExecute中完成
			break;
		case R.id.btn_play:
			
			player = new PlayTask();
			player.execute();

			break;
		case R.id.btn_finish:
			//完成播放
			this.isPlaying = false;
			break;
			
		}
	}
	
	class RecordTask extends AsyncTask<Void, Integer, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			isRecording = true;
			try {
				//开通输出流到指定的文件
				DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(audioFile)));
				//根据定义好的几个配置，来获取合适的缓冲大小
				int bufferSize = AudioRecord.getMinBufferSize(frequence, channelConfig, audioEncoding);
				//实例化AudioRecord
				AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, frequence, channelConfig, audioEncoding, bufferSize);
				//定义缓冲
				short[] buffer = new short[bufferSize];
				
				//开始录制
				record.startRecording();
				
				int r = 0; //存储录制进度
				//定义循环，根据isRecording的值来判断是否继续录制
				while(isRecording){
					//从bufferSize中读取字节，返回读取的short个数
					//这里老是出现buffer overflow，不知道是什么原因，试了好几个值，都没用，TODO：待解决
					int bufferReadResult = record.read(buffer, 0, buffer.length);
					//循环将buffer中的音频数据写入到OutputStream中
					for(int i=0; i<bufferReadResult; i++){
						dos.writeShort(buffer[i]);
					}
					publishProgress(new Integer(r)); //向UI线程报告当前进度
					r++; //自增进度值
				}
				//录制结束
				record.stop();
				Log.v("The DOS available:", "::"+audioFile.length());
				dos.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
		
		//当在上面方法中调用publishProgress时，该方法触发,该方法在UI线程中被执行
		protected void onProgressUpdate(Integer...progress){
			stateView.setText(progress[0].toString());
		}
		
		protected void onPostExecute(Void result){
			btnStop.setEnabled(false);
			btnStart.setEnabled(true);
			btnPlay.setEnabled(true);
			btnFinish.setEnabled(false);
		}
		
		protected void onPreExecute(){
			//stateView.setText("正在录制");
			btnStart.setEnabled(false);
			btnPlay.setEnabled(false);
			btnFinish.setEnabled(false);
			btnStop.setEnabled(true);		
		}
		
	}
	
	class PlayTask extends AsyncTask<Void, Integer, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			isPlaying = true;
			int bufferSize = AudioTrack.getMinBufferSize(frequence, channelConfig, audioEncoding);
			short[] buffer = new short[bufferSize/4];
			try {
				//定义输入流，将音频写入到AudioTrack类中，实现播放
				DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(audioFile)));
				//实例AudioTrack
				AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, frequence, channelConfig, audioEncoding, bufferSize, AudioTrack.MODE_STREAM);
				//开始播放
				track.play();
				//由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
				while(isPlaying && dis.available()>0){
					int i = 0;
					while(dis.available()>0 && i<buffer.length){
						buffer[i] = dis.readShort();
						i++;
					}
					//然后将数据写入到AudioTrack中
					track.write(buffer, 0, buffer.length);
					
				}
				
				//播放结束
				track.stop();
				dis.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
		
		protected void onPostExecute(Void result){
			btnPlay.setEnabled(true);
			btnFinish.setEnabled(false);
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
		}
		
		protected void onPreExecute(){	
			
			//stateView.setText("正在播放");
			btnStart.setEnabled(false);
			btnStop.setEnabled(false);
			btnPlay.setEnabled(false);
			btnFinish.setEnabled(true);			
		}
		
	}

}
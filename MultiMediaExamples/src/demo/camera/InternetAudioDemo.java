package demo.camera;

import java.io.IOException;
import java.util.List;

import demo.camera.M3UParser.FilePath;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

/**
 * Android支持播放网络上的Audio
 * 访问网络上的Audio，我们通过Http需要获取音频流
 * 这可能要涉及到ICY协议。ICY对Http协议进行了扩展
 * 然而，网络上的站点，往往并不允许我们直接访问其音频流
 * 我们需要一种中间文件来指向我们需要的音频流的地址，以使第三方的软件可以播放。
 * 对于ICY流来说，其就是一个PLS文件或者一个M3U文件
 * PLS对应的MIME类型为：audio/x-scpls
 * M3U对应的MIME类型为：audio/x-mpegurl
 * 
 * 虽然Android提供了对ICy流的支持，但是其并没有提供现成的方法来解析M3U或PLS文件
 * 所以，为了播放网络上的音频流，我们需要自己实现这些文件的解析
 * M3U文件其实就是一个音频流的索引文件，他指向要播放的音频流的路径。
 * @author Administrator
 *
 */
public class InternetAudioDemo extends ListActivity {
	
	private Button btnParse, btnPlay, btnStop;
	
	private EditText editUrl;
	
	private MediaPlayer player;
	
	private List<String> pathList;
	
	private int currPosition = 0; //记录当前播放的媒体文件的index
	
	//private ProgressDialog progress;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.internet_audio);
		
		btnParse = (Button)this.findViewById(R.id.btn_parse);
		btnPlay = (Button)this.findViewById(R.id.btn_start);
		btnStop = (Button)this.findViewById(R.id.btn_end);
		
		editUrl = (EditText)this.findViewById(R.id.edit_url);
		editUrl.setText("http://pubint.ic.llnwd.net/stream/pubint_kmfa.m3u");
//		InputMethodManager imm = (InputMethodManager)this.getSystemService(INPUT_METHOD_SERVICE);
//		imm.showSoftInput(editUrl, InputMethodManager.SHOW_IMPLICIT);
		btnPlay.setEnabled(false);
		btnStop.setEnabled(false);
		
		player = new MediaPlayer();
		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer player) {
				// 这个方法当MediaPlayer的play()执行完后触发
				player.stop();
				player.reset();
				if(pathList.size() > currPosition+1){
					currPosition++; //转到下一首
					prepareToPlay();
				}
			}
		});
		
		player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer arg0) {
				// 这个方法当MediaPlayer的prepare()执行完后触发
				btnStop.setEnabled(true);
				player.start();
				
				//当一曲播放完后，执行onCompletionListener的onCompletion方法
			}
		});
		
	}
	
	private void prepareToPlay(){
		try {
			//获取当前音频流的路径后我们需要通过MediaPlayer的setDataSource来设置，然后调用prepareAsync()来完成缓存加载
			String path = pathList.get(currPosition);
			player.setDataSource(path);
			//之所以使用prepareAsync是因为该方法是异步的，因为访问音频流是网络操作，在缓冲和准备播放时需要花费
			//较长的时间，这样用户界面就可能出现卡死的现象
			//该方法执行完成后，会执行onPreparedListener的onPrepared()方法。
			player.prepareAsync();
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	public void onClick(View v){
		int id = v.getId();
		switch(id){
		case R.id.btn_parse:
			//完成解析
//			progress = ProgressDialog.show(this, "提示", "正在解析，请稍后...");
//			progress.show();
			String url = null;
			if(editUrl.getText() != null){
				url = editUrl.getText().toString();
			}
			if(url != null && !url.trim().equals("")){
				pathList = M3UParser.parseStringFromUrl(url);
				ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pathList);
				this.setListAdapter(adapter);
				btnPlay.setEnabled(true);
			}else{
				Toast.makeText(this, "请输入正确的M3U文件访问地址", Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.btn_start:
			//这里播放是从第一个开始
			btnPlay.setEnabled(false);
			btnParse.setEnabled(false);
			this.currPosition = 0;
			if(pathList != null && pathList.size() > 0){
				
				prepareToPlay();
				
			}
			break;
		case R.id.btn_end:
			player.pause();
			btnPlay.setEnabled(true);
			btnStop.setEnabled(false);
			break;
		default:
			break;
			
		}
	}

}

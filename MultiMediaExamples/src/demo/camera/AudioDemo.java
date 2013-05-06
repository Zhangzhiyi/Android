package demo.camera;

import java.io.File;

import org.apache.http.client.utils.URIUtils;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.Albums;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * 本示例演示如何利用Android自带的Music来播放程序
 * 和Camera一样，可以通过Intent来启动它。
 * 我们需要指定一个ACTION_VIEW的Action
 * 同时一个Uri来指定我们要播放文件的路径
 * 最后指定一个MIME类型，指定所要播放的文件类型
 * 每种文件类型对应的都有一个MIME，他一般是类似于audio/mp3格式
 * 前部分是一个较大的类型，后面是更具体的类型
 * 
 * 同样的，对于Audio类型的多媒体，系统存储在MediaStore.Audio中
 * 包括Media，Album，Genre等信息体
 * 
 * 本文将以列表的形式列出所有的Album信息，供用户选择
 * 当用户选择某个Album时，系统将打开这个ALbum下的所有Audio
 * @author Administrator
 *
 */
public class AudioDemo extends ListActivity {
	
	private Button btnMusic;
	
	private boolean isAlbum = true; //true时，说明当前列表的内容是Album，false时，说明是Media
	
	private Cursor cursor; //游标对象，
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.audio);
		btnMusic = (Button)this.findViewById(R.id.btn_music);
		btnMusic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				Intent intent = new Intent(Intent.ACTION_VIEW);
//				//这里我们先从SDCard文件中获取指定文件的URi
//				File sdcard = Environment.getExternalStorageDirectory(); //这个是获取SDCard路径
//				File audioFile = new File(sdcard.getPath()+"/music/tt.mp3");
//				//然后需要获取该文件的Uri
//				Uri audioUri = Uri.fromFile(audioFile);
//				//然后指定Uri和MIME
//				intent.setDataAndType(audioUri, "audio/mp3");
//				startActivity(intent);
				
				//获取Album列表
				getAlbums();
				isAlbum = true;
			}
		});
		
	}
	
	public void onListItemClick(ListView l, View v, int position, long id){
		
		//判断当前是哪个列表
		if(isAlbum){
			//如果是Album，当用户点击某一个时，获取该Album下的所有media
			//l.getChildAt(position);
			if(cursor.moveToPosition(position)){
				getMedias(cursor.getInt(cursor.getColumnIndexOrThrow(Albums._ID)));
				isAlbum = false;
			}
		}else{
			//如果是Media，则当用户点击某一个时，则播放该Media
			//调用系统自带的MediaPlayer来播放
			if(cursor.moveToPosition(position)){
				String mediaUri = cursor.getString(cursor.getColumnIndexOrThrow(Audio.Media.DATA));
				String type = cursor.getString(cursor.getColumnIndexOrThrow(Audio.Media.MIME_TYPE));
				Uri data = Uri.fromFile(new File(mediaUri));
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(data, type);
				startActivity(intent);
			}
		}
		
		//super.onListItemClick(l, v, position, id);
	}
	
	
	//获取所有Albums
	public void getAlbums(){
		String[] columns = new String[]{
				Albums._ID,
				Albums.ALBUM
		};
		String[] from = new String[]{
				Albums.ALBUM
		};
		int[] to = new int[]{
				android.R.id.text1
		};
		cursor = this.managedQuery(Albums.EXTERNAL_CONTENT_URI, columns, null, null, Albums.DEFAULT_SORT_ORDER);
		CursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, from, to);
		this.setListAdapter(adapter);
		//this.isAlbum = true;
		
	}
	
	//获取某个Albums下对应的medias
	public void getMedias(int albumId){
		String[] columns = new String[]{
				Audio.Media._ID,
				Audio.Media.DATA,
				Audio.Media.DISPLAY_NAME,
				Audio.Media.MIME_TYPE
		};
		String selection = Audio.Media.ALBUM_ID + "=?";
		String[] selectionArgs = new String[]{
				albumId+""
		};
		
		String[] from = new String[]{
				Audio.Media.DISPLAY_NAME
		};
		int[] to = new int[]{
				android.R.id.text1
		};
		
		cursor = this.managedQuery(Audio.Media.EXTERNAL_CONTENT_URI, columns, selection, selectionArgs, Audio.Media.TITLE);
		CursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,cursor,from,to);
		this.setListAdapter(adapter);
	}

}

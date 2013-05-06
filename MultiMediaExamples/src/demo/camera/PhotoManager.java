package demo.camera;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 该类完成图片的检索，显示功能
 * @author Administrator
 *
 */
public class PhotoManager extends Activity {
	
	public static final float DISPLAY_WIDTH = 200;
	public static final float DISPLAY_HEIGHT = 200;
	
	//这里采用ImageButton的原因是有Button的作用
	private ImageButton photoView;
	private TextView nameView;
	
	private Cursor cursor;
	
	private String photoPath; //存放某张图片对应的位置信息
	private Bitmap currPhoto;
	
	//这三个变量主要用来保存Media.DATA,Media.TITLE,Media.DISPLAY_NAME的索引号，来获取每列的数据
	private int photoIndex;
	//private int titleIndex;
	private int nameIndex;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_view);
		
		photoView = (ImageButton)this.findViewById(R.id.image_view);
		photoView.setOnClickListener(clickListener);
		nameView = (TextView)this.findViewById(R.id.view_name);
		
		//指定获取的列
		String columns[] = new String[]{
				Media.DATA,Media._ID,Media.TITLE,Media.DISPLAY_NAME
		};
		//cursor = this.managedQuery(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
		cursor = this.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
		photoIndex = cursor.getColumnIndexOrThrow(Media.DATA);
		//titleIndex = cursor.getColumnIndexOrThrow(Media.TITLE);
		nameIndex = cursor.getColumnIndexOrThrow(Media.DISPLAY_NAME);
		
		Log.v("HERE First:", "First Debug");
		//显示第一张图片，但是首先要判断一下，Cursor是否有值
		if(cursor.moveToFirst()){
			showImage();
		}
	}
	
	private View.OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if(cursor.moveToNext()){
				showImage();
			}
		}
	};
	
	/**
	 * 显示图像信息
	 */
	private void showImage(){
		photoPath = cursor.getString(photoIndex); //这里获取到的就是图片存储的位置信息
		//这里怎样获取图片呢？看decodeBitmap
		Log.v("Photo Path:", photoPath);
		currPhoto = decodeBitmap(photoPath);
		photoView.setImageBitmap(currPhoto);
		nameView.setText(cursor.getString(nameIndex));		
	}
	
	/**
	 * 从path中获取图片信息
	 * @param path
	 * @return
	 */
	private Bitmap decodeBitmap(String path){
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, op); //获取尺寸信息
		//获取比例大小
		int wRatio = (int)Math.ceil(op.outWidth/DISPLAY_WIDTH);
		int hRatio = (int)Math.ceil(op.outHeight/DISPLAY_HEIGHT);
		//如果超出指定大小，则缩小相应的比例
		if(wRatio > 1 && hRatio > 1){
			if(wRatio > hRatio){
				op.inSampleSize = wRatio;
			}else{
				op.inSampleSize = hRatio;
			}
		}
		op.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(path, op);
		return bmp;
	}
	

}

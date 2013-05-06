package demo.camera;

import java.io.File;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
/**
 * 这里多媒体第一个示例，主要介绍Image的获取和存储
 * Image的获取可以通过Android自带的Camera应用来获得，
 * 图片的存储需要用到MediaStore对象。Android中的多媒体库。
 * 
 * @author Administrator
 *
 */
public class MainActivity extends Activity {
	
	private static final int RESULT_CODE = 1;
	private Button btnCamera;
	private ImageView imageView;
	
	private Uri imageFilePath;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        imageView = (ImageView)this.findViewById(R.id.imageView);
        btnCamera = (Button)this.findViewById(R.id.camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				
				/**
				 * 由于Camara返回的是缩略图，我们可以传递给他一个参数EXTRA_OUTPUT,
				 * 来将用Camera获取到的图片存储在一个指定的URI位置处。
				 * 下面就指定image存储在SDCard上，并且文件名为123.jpg
				 * imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"123.jpg";
				 * File file = new File(imageFilePath); //创建一个文件
				 * Uri imageUri = Uri.fromFile(file);
				 * 然而Android已经提供了一个多媒体库，那里统一存放了设备上所有的多媒体数据。所以，
				 * 我们可以将获取到的图片存放在那个多媒体库中。
				 * Android提供了MediaStore类，该类是一个ContentProvider，管理着设备上自带的和外部的多媒体文件，
				 * 同时包含着每一个多媒体文件的数据信息。
				 * 为了将数据存储在多媒体库，使用ContentResolver对象来操纵MediaStore对象
				 * 在MediaStore.Images.Media中有两个URI常量，一个是 	EXTERNAL_CONTENT_URI,另一个是INTERNAL_CONTENT_URI
				 * 第一个URI对应着外部设备(SDCard)，第二个URI对应着系统设备内部存储位置。
				 * 对于多媒体文件，一般比较大，我们选择外部存储方式
				 * 通过使用ContentResolver对象的insert方法我们可以向MediaStore中插入一条数据
				 * 这样在检索那张图片的时候，不再使用文件的路径，而是根据insert数据时返回的URI，获取一个InputStream
				 * 并传给BitmapFactory
				 */
				//在这里启动Camera。
				//Camera中定义了一个Intent-Filter，其中Action是android.media.action.IMAGE_CAPTURE
				//我们使用的时候，最好不要直接使用这个，而是用MediaStore中的常量ACTION_IMAGE_CAPTURE.
				//这个常量就是对应的上面的action
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			
				//这里我们插入一条数据，ContentValues是我们希望这条记录被创建时包含的数据信息
				//这些数据的名称已经作为常量在MediaStore.Images.Media中,有的存储在MediaStore.MediaColumn中了
				//ContentValues values = new ContentValues();
				ContentValues values = new ContentValues(3);
				values.put(MediaStore.Images.Media.DISPLAY_NAME, "testing");
				values.put(MediaStore.Images.Media.DESCRIPTION, "this is description");
				values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
				imageFilePath = MainActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath); //这样就将文件的存储方式和uri指定到了Camera应用中
				
				//由于我们需要调用完Camera后，可以返回Camera获取到的图片，
				//所以，我们使用startActivityForResult来启动Camera					
				startActivityForResult(intent, RESULT_CODE);
			}
		});
    }
    /**
     * 为了获取Camera返回的图片信息，重写该方法。
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode == RESULT_CODE){
    		//说明是由Camera返回的数据
    		//由Camera应用返回的图片数据是一个Camera对象，存储在一个名为data的extra域
    		//然后将获取到的图片存储显示在ImageView中
    		
    		try {
				Bundle extra = data.getExtras();
				/**
				 * 然而为了节约内存的消耗，这里返回的图片是一个121*162的缩略图。
				 * 那么如何返回我们需要的大图呢？看上面
				 * 然而存储了图片。有了图片的存储位置，能不能直接将图片显示出来呢》
				 * 这个问题就设计到对于图片的处理和显示，是非常消耗内存的，对于PC来说可能不算什么，但是对于手机来说
				 * 很可能使你的应用因为内存耗尽而死亡。不过还好，Android为我们考虑到了这一点
				 * Android中可以使用BitmapFactory类和他的一个内部类BitmapFactory.Options来实现图片的处理和显示
				 * BitmapFactory是一个工具类，里面包含了很多种获取Bitmap的方法。BitmapFactory.Options类中有一个inSampleSize，比如设定他的值为8，则加载到内存中的图片的大小将
				 * 是原图片的1/8大小。这样就远远降低了内存的消耗。
				 * BitmapFactory.Options op = new BitmapFactory.Options();
				 * op.inSampleSize = 8;
				 * Bitmap pic = BitmapFactory.decodeFile(imageFilePath, op);
				 * 这是一种快捷的方式来加载一张大图，因为他不用考虑整个显示屏幕的大小和图片的原始大小
				 * 然而有时候，我需要根据我们的屏幕来做相应的缩放，如何操作呢？
				 * 
				 */
				//首先取得屏幕对象
				Display display = this.getWindowManager().getDefaultDisplay();
				//获取屏幕的宽和高
				int dw = display.getWidth();
				int dh = display.getHeight();
				/**
				 * 为了计算缩放的比例，我们需要获取整个图片的尺寸，而不是图片
				 * BitmapFactory.Options类中有一个布尔型变量inJustDecodeBounds，将其设置为true
				 * 这样，我们获取到的就是图片的尺寸，而不用加载图片了。
				 * 当我们设置这个值的时候，我们接着就可以从BitmapFactory.Options的outWidth和outHeight中获取到值
				 */
				BitmapFactory.Options op = new BitmapFactory.Options();
				//op.inSampleSize = 8;
				op.inJustDecodeBounds = true;
				//Bitmap pic = BitmapFactory.decodeFile(imageFilePath, op);//调用这个方法以后，op中的outWidth和outHeight就有值了
				//由于使用了MediaStore存储，这里根据URI获取输入流的形式
				Bitmap pic = BitmapFactory.decodeStream(this
						.getContentResolver().openInputStream(imageFilePath),
						null, op);
				int wRatio = (int) Math.ceil(op.outWidth / (float) dw); //计算宽度比例
				int hRatio = (int) Math.ceil(op.outHeight / (float) dh); //计算高度比例
				Log.v("Width Ratio:", wRatio + "");
				Log.v("Height Ratio:", hRatio + "");
				/**
				 * 接下来，我们就需要判断是否需要缩放以及到底对宽还是高进行缩放。
				 * 如果高和宽不是全都超出了屏幕，那么无需缩放。
				 * 如果高和宽都超出了屏幕大小，则如何选择缩放呢》
				 * 这需要判断wRatio和hRatio的大小
				 * 大的一个将被缩放，因为缩放大的时，小的应该自动进行同比率缩放。
				 * 缩放使用的还是inSampleSize变量
				 */
				if (wRatio > 1 && hRatio > 1) {
					if (wRatio > hRatio) {
						op.inSampleSize = wRatio;
					} else {
						op.inSampleSize = hRatio;
					}
				}
				op.inJustDecodeBounds = false; //注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了
				pic = BitmapFactory.decodeStream(this.getContentResolver()
						.openInputStream(imageFilePath), null, op);
				imageView.setImageBitmap(pic);
			} catch (Exception e) {
				e.printStackTrace();
			} 
    	}
    }
}
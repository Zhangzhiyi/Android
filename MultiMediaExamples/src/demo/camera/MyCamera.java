package demo.camera;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
/**
 * Android自带的Camera应用程序可以完成很多功能。但是当其不能满足我们需要的时候
 * 我们可以定制自己的Camera。Android提供了Camera类来辅助我们实现自己的Camera。
 * 这个例子就来定义一个自己的Camera
 * 首先，在Manifest中需要引入权限<uses-permission android:name="android:permission.CAMERA"/>
 * 我们需要用来存放取景器的容器，这个容器就是SurfaceView。
 * 使用SurfaceView的同时，我们还需要使用到SurfaceHolder，SurfaceHolder相当于一个监听器，可以监听
 * Surface上的变化,通过其内部类CallBack来实现。
 * 为了可以获取图片，我们需要使用Camera的takePicture方法同时我们需要实现Camera.PictureCallBack类，实现onPictureTaken方法
 * @author Administrator
 *
 */
public class MyCamera extends Activity implements SurfaceHolder.Callback,Camera.PictureCallback{
	
	public static final int MAX_WIDTH = 200;
	public static final int MAX_HEIGHT = 200;
	
	private SurfaceView surfaceView;
	
	private Camera camera; //这个是hardare的Camera对象
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.camera);
		surfaceView = (SurfaceView)this.findViewById(R.id.myCameraView);
		surfaceView.setFocusable(true); 
		surfaceView.setFocusableInTouchMode(true);
		surfaceView.setClickable(true);
		surfaceView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				camera.takePicture(null, null, null, MyCamera.this);
				
			}
		});
		//SurfaceView中的getHolder方法可以获取到一个SurfaceHolder实例
		SurfaceHolder holder = surfaceView.getHolder();
		//为了实现照片预览功能，需要将SurfaceHolder的类型设置为PUSH
		//这样，画图缓存就由Camera类来管理，画图缓存是独立于Surface的
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// 当Surface被创建的时候，该方法被调用，可以在这里实例化Camera对象
		//同时可以对Camera进行定制
		camera = Camera.open(); //获取Camera实例
	
		
		/**
		 * Camera对象中含有一个内部类Camera.Parameters.该类可以对Camera的特性进行定制
		 * 在Parameters中设置完成后，需要调用Camera.setParameters()方法，相应的设置才会生效
		 * 由于不同的设备，Camera的特性是不同的，所以在设置时，需要首先判断设备对应的特性，再加以设置
		 * 比如在调用setEffects之前最好先调用getSupportedColorEffects。如果设备不支持颜色特性，那么该方法将
		 * 返回一个null
		 */

		try {
			
			Camera.Parameters param = camera.getParameters();
			if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
				//如果是竖屏
				param.set("orientation", "portrait");
				//在2.2以上可以使用
				//camera.setDisplayOrientation(90);
			}else{
				param.set("orientation", "landscape");
				//在2.2以上可以使用
				//camera.setDisplayOrientation(0);				
			}
			//首先获取系统设备支持的所有颜色特效，有复合我们的，则设置；否则不设置
			List<String> colorEffects = param.getSupportedColorEffects();
			Iterator<String> colorItor = colorEffects.iterator();
			while(colorItor.hasNext()){
				String currColor = colorItor.next();
				if(currColor.equals(Camera.Parameters.EFFECT_SOLARIZE)){
					param.setColorEffect(Camera.Parameters.EFFECT_SOLARIZE);
					break;
				}
			}
			//设置完成需要再次调用setParameter方法才能生效
			camera.setParameters(param);
			
			camera.setPreviewDisplay(holder);
			
			/**
			 * 在显示了预览后，我们有时候希望限制预览的Size
			 * 我们并不是自己指定一个SIze而是指定一个Size，然后
			 * 获取系统支持的SIZE，然后选择一个比指定SIZE小且最接近所指定SIZE的一个
			 * Camera.Size对象就是该SIZE。
			 * 
			 */
			int bestWidth = 0;
			int bestHeight = 0;
			
			List<Camera.Size> sizeList = param.getSupportedPreviewSizes();
			//如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
			if(sizeList.size() > 1){
				Iterator<Camera.Size> itor = sizeList.iterator();
				while(itor.hasNext()){
					Camera.Size cur = itor.next();
					if(cur.width > bestWidth && cur.height>bestHeight && cur.width <MAX_WIDTH && cur.height < MAX_HEIGHT){
						bestWidth = cur.width;
						bestHeight = cur.height;
					}
				}
				if(bestWidth != 0 && bestHeight != 0){
					param.setPreviewSize(bestWidth, bestHeight);
					//这里改变了SIze后，我们还要告诉SurfaceView，否则，Surface将不会改变大小，进入Camera的图像将质量很差
					surfaceView.setLayoutParams(new LinearLayout.LayoutParams(bestWidth, bestHeight));
				}
			}
			camera.setParameters(param);
			camera.startPreview();
		} catch (Exception e) {
			// 如果出现异常，则释放Camera对象
			camera.release();
		}
		
		//启动预览功能
		
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// 当Surface被销毁的时候，该方法被调用
		//在这里需要释放Camera资源
		camera.stopPreview();
		camera.release();
		
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		// data是一个原始的JPEG图像数据，
		//在这里我们可以存储图片，很显然可以采用MediaStore
		//注意保存图片后，再次调用startPreview()回到预览
		Uri imageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
		try {
			OutputStream os = this.getContentResolver().openOutputStream(imageUri);
			os.write(data);
			os.flush();
			os.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		camera.startPreview();
	}
	
	
	
}

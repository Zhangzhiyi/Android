package demo.camera;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 在Android中我们可以对图像进行编辑处理等操作
 * 包括放大缩小，旋转，偏移，裁剪，以及更改亮度，饱和度等
 * 
 * 1、首先，从SDCard中选择图片，采用Android自带的Callery应用获得
 * Gallery是Android自带的图片和视频管理应用
 * 使用Intent来启动Gallery应用，需要指定两个参数，一个是Action，另一个是多媒体存放的URI
 * Action是一个通用的Action叫ACTION_PICK，来告诉Gallery，我们想检索数据。
 * 第二个是Data，是一个URI，这里当然是MediaStore.Images.Media.EXTERNAL_CONTENT_URI
 * 当在Gallery中选择了一个图片的时候，返回的Intent中的Data域就是所选图片对应的URI
 * 
 * @author Administrator
 *
 */
public class PhotoProcess extends Activity{
	public static final int FIRST_PIC = 0;
	public static final int SECOND_PIC = 1;
	public static final int MAX_WIDTH = 240;
	public static final int MAX_HEIGHT = 180;
	private Button btnSelect,btnSelect2;
	private ImageView srcImageView, dstImageView;
	
	private Bitmap srcBitmap, dstBitmap;
	private Uri imageUri;
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.process);
		
		this.btnSelect = (Button)this.findViewById(R.id.btn_select);
		btnSelect.setOnClickListener(clickListener);
		this.btnSelect2 = (Button)this.findViewById(R.id.btn_select2);
		btnSelect2.setOnClickListener(clickListener2);
		srcImageView = (ImageView)this.findViewById(R.id.img_src);
		dstImageView = (ImageView)this.findViewById(R.id.img_dst);
	}
	
	private View.OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// 启动Gallery应用
			Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, FIRST_PIC);
		}
	};
	private View.OnClickListener clickListener2 = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// 启动Gallery应用
			Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, SECOND_PIC);
			
		}
	};	
	
	public boolean onCreateOptionsMenu(Menu menu){
		//super.onCreateOptionsMenu(menu);
		//MenuInflater menuInflater = new MenuInflater(this);
		//menuInflater.inflate(R.layout.image, menu)
		menu.add(Menu.NONE,1,Menu.NONE,"复制");
		menu.add(Menu.NONE,2,Menu.NONE,"变换");
		menu.add(Menu.NONE,3,Menu.NONE,"亮度");
		menu.add(Menu.NONE,4,Menu.NONE,"合成");
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		switch(id){
		case 1:
			//复制一个图像
			if(srcBitmap != null){
				dstBitmap = getDstImage(null);//这里没有变换
				dstImageView.setImageBitmap(dstBitmap);
			}
			break;
		case 2:
			//对复制后的图像进行变换
			if(srcBitmap != null){
				dstBitmap = transferImage();
				dstImageView.setImageBitmap(dstBitmap);
			}
			break;
		case 3:
			//改变图像的色彩
			if(srcBitmap != null){
				dstBitmap = ajustImage();
				dstImageView.setImageBitmap(dstBitmap);
			}
			break;
		case 4:
			if(srcBitmap != null && dstBitmap != null){
				dstBitmap = compositeImages();
				dstImageView.setImageBitmap(dstBitmap);
			}
			break;
		}
		return true;
	}
	
	/**
	 * 为了创建一个图像的副本，我们可以在创建一个新的空的Bitmap，然后在这个Bitmap上绘制一个Bitmap
	 * 这个空的Bitmap应该和已存在的Bitmap具有相同的尺寸和颜色深度
	 * 
	 * 然后我们需要一个Canvas对象，一个Canvas简单说，就是一个画布，存放Bitmap，在构造时，就可以传入Bitmap对象
	 * 同时，Canvas中定义了很多便捷的画图方法，方便我们绘制各种图形
	 * 接下来，如果我们需要处理颜色和对比度，我们需要一个Paint对象，通过Paint我们可以设置画笔的各种特性。
	 * 
	 * 最后，我们调用Canvas的drawBitmap就可以将原Bitmap绘制在dstBitmap上了
	 * 
	 */
	private Bitmap getDstImage(Matrix matrix){
		
		Bitmap bmp = null;

		//下面这个Bitmap中创建的函数就可以创建一个空的Bitmap
		//返回的是一个可以改变的Bitmap对象，这样我们后面就可以对其进行变换和颜色调整等操作了
		bmp = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
		//创建Canvas对象，
		Canvas canvas = new Canvas(bmp); 
		//创建Paint对象，这里先不用
		Paint paint = new Paint();
		//在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
		
		if(matrix != null){
			//如果matrix存在，则采用变换
			canvas.drawBitmap(dstBitmap, matrix, paint);
		}else{
			canvas.drawBitmap(srcBitmap, 0, 0, paint);
		}
		
		
		return bmp;


	}
	
	
	/**
	 * 重载getDstImage函数，传入定制的Paint对象
	 * @param matrix
	 * @param paint
	 * @return
	 */
	private Bitmap getDstImage(Matrix matrix, Paint paint){
		
		Bitmap bmp = null;

		//下面这个Bitmap中创建的函数就可以创建一个空的Bitmap
		//返回的是一个可以改变的Bitmap对象，这样我们后面就可以对其进行变换和颜色调整等操作了
		bmp = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
		//创建Canvas对象，
		Canvas canvas = new Canvas(bmp); 
		
		//在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
		
		if(matrix != null){
			//如果matrix存在，则采用变换
			canvas.drawBitmap(dstBitmap, matrix, paint);
		}else{
			canvas.drawBitmap(srcBitmap, 0, 0, paint);
		}
		
		
		return bmp;


	}	
	
	/**
	 * 为了放大缩小、旋转图像，我们要使用Matrix类。Matrix类是一个三维矩阵。
	 * 在Android屏幕中，图像的每个像素对应都是一个坐标，一个坐标由x/y/z组成
	 * ------------------------
	 * cosX -sinX translateX
	 * sinX cosX  translateY
	 * 0	0	  scale
	 * ------------------------
	 * 第一行的值，影响着x坐标。比如 1 0 0 =>x = 1*x + 0*y + 0*z
	 * 第二行的值，影响着y坐标。比如0 1 0 => y = 0*x + 1*y + 0*z
	 * 第三行的值，影响着z坐标。比如 0 0 1 => z = 0*x + 0*y + 1*z
	 * 
	 * 我们自己计算一个矩阵然后通过Matrax.setValues设置。
	 * 这样，在调用canvas的drawBitmap方法时，传入matrix
	 * 
	 * Matrix类并不提倡我们使用这种方式来操作变换，Matrix针对不同的变换都相应的有pre，set，post三种方法
	 * 可以使用。
	 * pre是矩阵前乘
	 * set是直接设置
	 * post是矩阵后乘
	 */
	private Bitmap transferImage(){
		Matrix matrix = new Matrix();
		matrix.setValues(new float[]{
			.5f,0,0,//这里只会影响到x轴，所以，图片的长度将是原来的一半
			0,1,0,
			0,0,1
		});
		return this.getDstImage(matrix);
	}
	
	/**
	 * 该方法中我们将对图像的颜色，亮度，对比度等进行设置
	 * 需要用到ColorMatrix类。ColorMatrix类是一个四行五列的矩阵
	 * 每一行影响着[R,G,B,A]中的一个
	 * -------------------------
	 * a1 b1 c1 d1 e1
	 * a2 b2 c2 d2 e2
	 * a3 b3 c3 d3 e3
	 * a4 b4 c4 d4 e4
	 * -------------------------
	 * Rnew => a1*R+b1*G+c1*B+d1*A+e1
	 * Gnew => a2*R+b2*G+c2*B+d2*A+e2
	 * Bnew => a3*R+b3*G+c3*B+d3*A+e3
	 * Gnew => a4*R+b4*G+c4*B+d4*A+e4
	 * 其中R，G，B的值是128，A的值是0
	 * 
	 * 最后将颜色的修改，通过Paint.setColorFilter应用到Paint对象中。
	 * 主要对于ColorMatrix,需要将其包装成ColorMatrixColorFilter对象，再传给Paint对象
	 * 
	 * 同样的，ColorMatrix提供给我们相应的方法，setSaturation()就可以设置一个饱和度
	 */
	private Bitmap ajustImage(){
		ColorMatrix cMatrix = new ColorMatrix();
//		int brightIndex = -25;
//		int doubleColor = 2;
//		cMatrix.set(new float[]{
//				doubleColor,0,0,0,brightIndex, //这里将1改为2则我们让Red的值为原来的两倍
//				0,doubleColor,0,0,brightIndex,//改变最后一列的值，我们可以不改变RGB同道颜色的基础上，改变亮度
//				0,0,doubleColor,0,brightIndex,
//				0,0,0,doubleColor,0
//		});
		//cMatrix.setSaturation(2.0f);//设置饱和度
		cMatrix.setScale(2.0f, 2.0f, 2.0f, 2.0f);//设置颜色同道色彩缩放
		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
		return this.getDstImage(null, paint);
	}
	
	/**
	 * 图像的合成，可以通过在同一个Canvas中绘制两张图片。
	 * 只是在绘制第二章图片的时候，需要给Paint指定一个变幻模式TransferMode。
	 * 在Android中有一个XFermode所有的变幻模式都是这个类的子类
	 * 我们需要用到它的一个子类PorterDuffXfermode,关于这个类，其中用到PorterDuff类
	 * 这个类很简单，就包含一个Enum是Mode，其中定义了一组规则，这组规则就是如何将
	 * 一张图像和另一种图像进行合成
	 * 关于图像合成有四种模式，LIGHTEN,DRAKEN,MULTIPLY,SCREEN
	 */
	private Bitmap compositeImages(){
		
		Bitmap bmp = null;

		//下面这个Bitmap中创建的函数就可以创建一个空的Bitmap
		bmp = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
		Paint paint = new Paint();
		Canvas canvas = new Canvas(bmp);
		//首先绘制第一张图片，很简单，就是和方法中getDstImage一样
		canvas.drawBitmap(srcBitmap, 0, 0, paint);		
		
		//在绘制第二张图片的时候，我们需要指定一个Xfermode
		//这里采用Multiply模式，这个模式是将两张图片的对应的点的像素相乘
		//，再除以255，然后以新的像素来重新绘制显示合成后的图像
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
		canvas.drawBitmap(dstBitmap, 0, 0, paint);
		
		return bmp;
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.v("Result OK Value:", resultCode+"");
		Log.v("RequestCode Value", requestCode+"");
		
		if(resultCode == RESULT_OK){
			imageUri = data.getData();	
			if(requestCode == FIRST_PIC){
				//在Gallery中选中一个图片时，返回来的Intent中的Data就是选择图片的Uri
				srcBitmap = getSrcImage(imageUri);
				srcImageView.setImageBitmap(srcBitmap);				
			}else if(requestCode == SECOND_PIC){
				//这里处理用户选择的第二张图片
				
				dstBitmap = getSrcImage(imageUri);
				dstImageView.setImageBitmap(dstBitmap);
			}

		}
	}
	
	/**
	 * 需要加载的图片可能是大图，我们需要对其进行合适的缩小处理
	 * @param imageUri
	 */
	private Bitmap getSrcImage(Uri imageUri){
		//Display display = this.getWindowManager().getDefaultDisplay();
		try {
			BitmapFactory.Options ops = new BitmapFactory.Options();
			ops.inJustDecodeBounds = true;
			Bitmap bmp = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri),null,ops);
			int wRatio = (int)Math.ceil(ops.outWidth/(float)MAX_WIDTH);
			int hRatio = (int)Math.ceil(ops.outHeight/(float)MAX_HEIGHT);
			
			if(wRatio > 1 && hRatio > 1){
				if(wRatio > hRatio){
					ops.inSampleSize = wRatio;
				}else{
					ops.inSampleSize = hRatio;
				}
			}
			
			ops.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri),null,ops);
			
			return bmp;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(this.getClass().getName(), e.getMessage());
		}
		
		return null;
	}

}

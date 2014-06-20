package com.test.testcropimage;

import java.io.FileNotFoundException;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Build;
import android.provider.MediaStore;

public class MainActivity extends ActionBarActivity {
	
	public final static int CHOOSE_SMALL_PICTURE = 1001;
	public final static int CHOOSE_BIG_PICTURE = 1002;
	public final static int TAKE_BIG_PICTURE = 1003;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
    	private Button mChooseButton;
    	private Button mClipButton;
    	private Button mCaptureButton;
    	private ImageView mImageView;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mChooseButton = (Button) rootView.findViewById(R.id.button1);
            mClipButton = (Button) rootView.findViewById(R.id.button2);
            mCaptureButton = (Button) rootView.findViewById(R.id.button3);
            mImageView = (ImageView) rootView.findViewById(R.id.imageView1);
            return rootView;
        }
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
        	// TODO Auto-generated method stub
        	super.onViewCreated(view, savedInstanceState);
        	mCaptureButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
					intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
					startActivityForResult(intent, TAKE_BIG_PICTURE);
				}
			});
        	mChooseButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//通过相册选择图片并裁剪
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
					intent.setType("image/*");
					intent.putExtra("crop", "true");
					intent.putExtra("aspectX", 1);
					intent.putExtra("aspectY", 1);
					intent.putExtra("outputX", 100);
					intent.putExtra("outputY", 100);
					intent.putExtra("scale", true);
					intent.putExtra("return-data", true);
					intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
					intent.putExtra("noFaceDetection", true); // no face detection
					startActivityForResult(intent, CHOOSE_SMALL_PICTURE);

				}
			});
        	mClipButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

						Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
						intent.setType("image/*");
			    		intent.putExtra("crop", "true");
			    		intent.putExtra("aspectX", 2);
			    		intent.putExtra("aspectY", 1);
			    		intent.putExtra("outputX", 500);
			    		intent.putExtra("outputY", 500);
			    		intent.putExtra("scale", true);
			    		intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
			    		intent.putExtra("return-data", false);
			    		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			    		intent.putExtra("noFaceDetection", true); // no face detection
						startActivityForResult(intent, CHOOSE_BIG_PICTURE);
		
				}
			});
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode,
        		Intent data) {
        	// TODO Auto-generated method stub
        	super.onActivityResult(requestCode, resultCode, data);
        	if (resultCode == RESULT_OK) {
				if (requestCode == CHOOSE_SMALL_PICTURE) {
					Uri uri = data.getData();
					if (uri != null) {
						mImageView.setImageURI(uri);
					}else{
						Bitmap bitmap = data.getParcelableExtra("data");
						mImageView.setImageBitmap(bitmap);
					}
				}else if (requestCode == CHOOSE_BIG_PICTURE){
					Uri uri = getTempUri();
					try {
						Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
						mImageView.setImageBitmap(bitmap);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if (requestCode == TAKE_BIG_PICTURE){
					cropImageUri(getTempUri(), 780, 600, CHOOSE_BIG_PICTURE);
				}
			}
        }
        public Uri getTempUri(){
        	String filePath = "file://" + Environment.getExternalStorageDirectory() + "/temp.jpg";
			Uri outPutUri = Uri.parse(filePath);
			return outPutUri;
        }
        public void cropImageUri(Uri uri, int outputX, int outputY, int requestCode){
    		Intent intent = new Intent("com.android.camera.action.CROP");
    		intent.setDataAndType(uri, "image/*");
    		intent.putExtra("crop", "true");
    		intent.putExtra("aspectX", 2);
    		intent.putExtra("aspectY", 1);
    		intent.putExtra("outputX", outputX);
    		intent.putExtra("outputY", outputY);
    		intent.putExtra("scale", true);
    		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    		intent.putExtra("return-data", false);
    		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    		intent.putExtra("noFaceDetection", true); // no face detection
    		startActivityForResult(intent, requestCode);
    	}
    }

}

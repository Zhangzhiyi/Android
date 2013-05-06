package lab.sodino.menutest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivyt extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    }
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = new MenuInflater(getApplicationContext());
		inflater.inflate(R.menu.menu, menu);
		setMenuBackground();
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		String info = "";
		switch (item.getItemId()) {
		case R.id.menu_add:
			info = "Add";
			break;
		case R.id.menu_delete:
			info = "Delete";
			break;
		case R.id.menu_home:
			info = "Home";
			break;
		case R.id.menu_help:
			info = "Help";
			break;
		default:
			info = "NULL";
			break;
		}
		Toast toast = Toast.makeText(this, info, Toast.LENGTH_SHORT);
		toast.show();
		return super.onOptionsItemSelected(item);
	}

	// 关键代码就是重写Layout类的工厂方法onCreateView
	protected void setMenuBackground() {
		getLayoutInflater().setFactory(new android.view.LayoutInflater.Factory() {
			/**
			 * name - Tag name to be inflated.<br/>
			 * context - The context the view is being created in.<br/>
			 * attrs - Inflation attributes as specified in XML file.<br/>
			 */
			public View onCreateView(String name, Context context, AttributeSet attrs) {
				// 下面这句仅针对原生系统
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
					try {
						LayoutInflater f = getLayoutInflater();
						// 尝试创建我们自己布局
						final View view = f.createView(name, null, attrs);
						new Handler().post(new Runnable() {
							public void run() {
								// 设置背景图片
								view.setBackgroundResource(R.drawable.menu_background);
							}
						});
						return view;
					} catch (InflateException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		});
	}
}
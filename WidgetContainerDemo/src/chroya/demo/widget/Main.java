package chroya.demo.widget;

import static android.util.Log.d;

import java.util.ArrayList;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;

/**
 * 添加appwidget
 * @author chroya
 *
 */
public class Main extends Activity {
	private AppWidgetHost mAppWidgetHost;
	private AppWidgetManager mAppWidgetManager;
	private WidgetLayout layout;	
	
	private static final int REQUEST_PICK_APPWIDGET = 1;
	private static final int REQUEST_CREATE_APPWIDGET = 2;	
	private static final int APPWIDGET_HOST_ID = 0x100;
	private static final String EXTRA_CUSTOM_WIDGET = "custom_widget";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mAppWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        mAppWidgetHost = new AppWidgetHost(getApplicationContext(), APPWIDGET_HOST_ID);
        //开始监听widget的变化
        mAppWidgetHost.startListening();
        
        layout = new WidgetLayout(this);
        layout.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				
				addWidget();
				return false;
			}
		});
        setContentView(layout);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK) {
    		switch (requestCode) {
    		case REQUEST_PICK_APPWIDGET:
                addAppWidget(data);
                break;
            case REQUEST_CREATE_APPWIDGET:
                completeAddAppWidget(data);
                break;
    		}
    	} else if (requestCode == REQUEST_PICK_APPWIDGET &&
                resultCode == RESULT_CANCELED && data != null) {
            // Clean up the appWidgetId if we canceled
            int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1) {
                mAppWidgetHost.deleteAppWidgetId(appWidgetId);
            }
        }
    }
    
    /**
     * 选中了某个widget之后，根据是否有配置来决定直接添加还是弹出配置activity
     * @param data
     */
    private void addAppWidget(Intent data) {
        int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

        String customWidget = data.getStringExtra(EXTRA_CUSTOM_WIDGET);
        d("addAppWidget", "data:"+ customWidget);
        if ("search_widget".equals(customWidget)) {
        	//这里直接将search_widget删掉了
            mAppWidgetHost.deleteAppWidgetId(appWidgetId);
        } else {
            AppWidgetProviderInfo appWidget = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
            
            d("addAppWidget", "configure:"+ appWidget.configure);
            if (appWidget.configure != null) {
            	//有配置，弹出配置
                Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
                intent.setComponent(appWidget.configure);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

                startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
            } else {
            	//没有配置，直接添加
                onActivityResult(REQUEST_CREATE_APPWIDGET, Activity.RESULT_OK, data);
            }
        }
    }
    
    /**
     * 请求添加一个新的widget
     */
    private void addWidget() {
    	int appWidgetId = mAppWidgetHost.allocateAppWidgetId();
    	
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // add the search widget
        ArrayList<AppWidgetProviderInfo> customInfo =
                new ArrayList<AppWidgetProviderInfo>();
        AppWidgetProviderInfo info = new AppWidgetProviderInfo();
        info.provider = new ComponentName(getPackageName(), "XXX.YYY");
        info.label = "Search";
        info.icon = R.drawable.ic_search_widget;
        customInfo.add(info);
        pickIntent.putParcelableArrayListExtra(
                AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
        ArrayList<Bundle> customExtras = new ArrayList<Bundle>();
        Bundle b = new Bundle();
        b.putString(EXTRA_CUSTOM_WIDGET, "search_widget");
        customExtras.add(b);
        pickIntent.putParcelableArrayListExtra(
                AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
        // start the pick activity
        startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
    }    
    
    /**
     * 添加widget
     * @param data
     */
    private void completeAddAppWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

        d("completeAddAppWidget", "dumping extras content="+extras.toString());
        d("completeAddAppWidget", "appWidgetId:"+ appWidgetId);
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        
        View hostView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);
        
        layout.addInScreen(hostView, appWidgetInfo.minWidth, appWidgetInfo.minHeight);        
    }
}
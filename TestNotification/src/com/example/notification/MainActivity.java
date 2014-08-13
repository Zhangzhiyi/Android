package com.example.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigPictureStyle;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private static final int NOTIFICATION_ID_1 = 0;
	private static final int NOTIFICATION_ID_2 = 1;
	private static final int NOTIFICATION_ID_3 = 2;
	private static final int NOTIFICATION_ID_4 = 3;
	private static final int NOTIFICATION_ID_5 = 4;
	private static final int NOTIFICATION_ID_6 = 5;
	private static final int NOTIFICATION_ID_7 = 6;
	private static final int NOTIFICATION_ID_8 = 7;

	private static int messageNum = 0;
	private Context context = this;
	private NotificationManager manager;
	private Bitmap icon;
	private static final int[] btns = new int[] { R.id.btn1, R.id.btn2,
			R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8,
			R.id.btn9 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		// 获取通知服务
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 注册监听器
		for (int btn : btns) {
			findViewById(btn).setOnClickListener(this);
		}

		icon = BitmapFactory.decodeResource(getResources(),
				R.drawable.wandoujia);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn1:
			showNormal();
			break;
		case R.id.btn2:
			showBigView_Text();
			break;
		case R.id.btn3:
			showBigView_Pic();
			break;
		case R.id.btn4:
			showBigView_Inbox();
			break;
		case R.id.btn5:
			showCustomView();
			break;
		case R.id.btn6:
			backApp();
			break;
		case R.id.btn7:
			backScreen();
			break;
		case R.id.btn8:
			showProgressBar();
			break;
		case R.id.btn9:
			dismiss();
			break;
		default:
			Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private void dismiss() {
		manager.cancelAll();
	}

	private void showCustomView() {
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.custom_notification);
		Intent intent = new Intent(this, TestMusicControl.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.paly_pause_music,
				pendingIntent);
		NotificationCompat.Builder builder = new Builder(context);
		builder.setSmallIcon(R.drawable.music_icon)
				.setLargeIcon(icon).setOngoing(true)
				.setTicker("music is playing")
				.setContent(remoteViews);
		manager.notify(NOTIFICATION_ID_8, builder.build());
	}

	private void showProgressBar() {

		final NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		builder.setLargeIcon(icon).setSmallIcon(R.drawable.ic_launcher)
				.setTicker("showProgressBar").setContentInfo("contentInfo")
				.setOngoing(true).setContentTitle("ContentTitle")
				.setContentText("ContentText");
		new Thread(new Runnable() {

			@Override
			public void run() {

				int progress = 0;

				for (progress = 0; progress < 100; progress += 5) {
					//将setProgress的第三个参数设为true即可显示为无明确进度的进度条样式
					builder.setProgress(100, progress, false);
					manager.notify(NOTIFICATION_ID_7, builder.build());
					try {
						// Sleep for 5 seconds
						Thread.sleep(2 * 1000);
					} catch (InterruptedException e) {
						System.out.println("sleep failure");
					}
				}
				builder.setContentTitle("Download complete")
						.setProgress(0, 0, false).setOngoing(false);
				manager.notify(NOTIFICATION_ID_7, builder.build());

			}
		}).start();
	}

	private void backScreen() {
		Intent notifyIntent = new Intent(this, SpecialActivity.class);
		// Sets the Activity to start in a new, empty task
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// Creates the PendingIntent
		PendingIntent notify_Intent = PendingIntent.getActivity(this, 0,
				notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new NotificationCompat.Builder(context)
				.setLargeIcon(icon).setSmallIcon(R.drawable.ic_launcher)
				.setTicker("backScreen").setContentInfo("contentInfo")
				.setContentTitle("ContentTitle").setContentText("ContentText")
				.setContentIntent(notify_Intent).setAutoCancel(true)
				.setDefaults(Notification.DEFAULT_ALL).build();
		manager.notify(NOTIFICATION_ID_6, notification);
		this.finish();
	}

	private void backApp() {

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack
		stackBuilder.addParentStack(OtherActivity.class);
		// Adds the Intent to the top of the stack
		Intent resultIntent = new Intent(this, OtherActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		// Gets a PendingIntent containing the entire back stack
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new NotificationCompat.Builder(context)
				.setLargeIcon(icon).setSmallIcon(R.drawable.ic_launcher)
				.setTicker("backApp").setContentInfo("contentInfo")
				.setContentTitle("ContentTitle").setContentText("ContentText")
				.setContentIntent(resultPendingIntent).setAutoCancel(true)
				.setDefaults(Notification.DEFAULT_ALL).build();
		manager.notify(NOTIFICATION_ID_5, notification);
		this.finish();
	}

	private void showBigView_Inbox() {
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		inboxStyle.setBigContentTitle("BigContentTitle").setSummaryText(
				"SummaryText");
		for (int i = 0; i < 10; i++)
			inboxStyle.addLine("news:" + i);
		Notification notification = new NotificationCompat.Builder(context)
				.setLargeIcon(icon).setSmallIcon(R.drawable.ic_launcher)
				.setTicker("showBigView_Inbox").setContentInfo("contentInfo")
				.setContentTitle("ContentTitle").setContentText("ContentText")
				.setStyle(inboxStyle)
				.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.build();
		manager.notify(NOTIFICATION_ID_4, notification);
	}

	private void showBigView_Pic() {
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.custom_notification);
		Intent notifyIntent = new Intent(this, SpecialActivity.class);
		// Sets the Activity to start in a new, empty task
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// Creates the PendingIntent
		PendingIntent notify_Intent = PendingIntent.getActivity(this, 0,
				notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.BigPictureStyle pictureStyle = new BigPictureStyle();
		pictureStyle.setBigContentTitle("BigContentTitle").bigPicture(icon)
				.setSummaryText("SummaryText");
		Notification notification = new NotificationCompat.Builder(context)
				.setLargeIcon(icon).setSmallIcon(R.drawable.ic_launcher)
				.setTicker("showBigView_Pic").setContentInfo("contentInfo")
				.setContentTitle("ContentTitle").setContentText("ContentText")
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setStyle(pictureStyle)
				.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
				.addAction(R.drawable.ic_stat_snooze, getString(R.string.snooze), notify_Intent)
				.build();
		notification.bigContentView = remoteViews;
		manager.notify(NOTIFICATION_ID_3, notification);
	}

	private void showBigView_Text() {
		Intent notifyIntent = new Intent(this, SpecialActivity.class);
		// Sets the Activity to start in a new, empty task
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// Creates the PendingIntent
		PendingIntent notify_Intent = PendingIntent.getActivity(this, 0,
				notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.BigTextStyle textStyle = new BigTextStyle();
		textStyle
				.setBigContentTitle("BigContentTitle")
				.setSummaryText("SummaryText")
				.bigText(
						"I am Big Texttttttttttttttttttttttttttttttttttttttttttt!!!!!!!!!!!!!!!!!!!......");
		Notification notification = new NotificationCompat.Builder(context)
				.setLargeIcon(icon).setSmallIcon(R.drawable.ic_launcher)
				.setTicker("showBigView_Text").setContentInfo("contentInfo")
				.setContentTitle("ContentTitle").setContentText("ContentText")
				.setStyle(textStyle)
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
				.addAction(R.drawable.ic_stat_snooze, getString(R.string.snooze), notify_Intent)
				.build();
		manager.notify(NOTIFICATION_ID_2, notification);
	}

	private void showNormal() {
		Intent notifyIntent = new Intent(this, SpecialActivity.class);
		// Sets the Activity to start in a new, empty task
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// Creates the PendingIntent
		PendingIntent notify_Intent = PendingIntent.getActivity(this, 0,
				notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Intent deleteReceiver = new Intent(this, DeleteNotificationReceiver.class);
		PendingIntent deleteIntent = PendingIntent.getBroadcast(this, 0, deleteReceiver, 0);
		
		Notification notification = new NotificationCompat.Builder(context)
				.setDeleteIntent(deleteIntent)
				.setLargeIcon(icon).setSmallIcon(R.drawable.ic_launcher)
				.setTicker("showNormal").setContentInfo("contentInfo")
				.setContentIntent(notify_Intent)
				.setContentTitle("ContentTitle").setContentText("ContentText")
				.setNumber(++messageNum)
				.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
				.build();
		manager.notify(NOTIFICATION_ID_1, notification);
	}
	
	public static class DeleteNotificationReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("DeleteNotificationReceiver", "onReceive");
		}
	}
}

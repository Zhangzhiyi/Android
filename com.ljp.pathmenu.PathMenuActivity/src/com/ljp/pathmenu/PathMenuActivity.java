package com.ljp.pathmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PathMenuActivity extends Activity {

	private boolean areButtonsShowing;
	private RelativeLayout composerButtonsWrapper;
	private ImageView composerButtonsShowHideButtonIcon;

	private RelativeLayout composerButtonsShowHideButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		MyAnimations.initOffset(PathMenuActivity.this);

		findViews();
		setListener();

		// 加号的动画
		composerButtonsShowHideButton.startAnimation(MyAnimations.getRotateAnimation(0, 360, 200));

	}

	private void findViews() {
		composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
		composerButtonsShowHideButton = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
		composerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon);
	}

	private void setListener() {
		// 给大按钮设置点击事件
		composerButtonsShowHideButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!areButtonsShowing) {
					// 图标的动画
					MyAnimations.startAnimationsIn(composerButtonsWrapper, 300);
					// 加号的动画
					composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(0, -225, 300));
				} else {
					// 图标的动画
					MyAnimations.startAnimationsOut(composerButtonsWrapper, 300);
					// 加号的动画
					composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(-225, 0, 300));
				}
				areButtonsShowing = !areButtonsShowing;
			}
		});

		// 给小图标设置点击事件
		for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {
			final ImageView smallIcon = (ImageView) composerButtonsWrapper.getChildAt(i);
			final int position = i;
			smallIcon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// 这里写各个item的点击事件
					// 1.加号按钮缩小后消失 缩小的animation
					// 2.其他按钮缩小后消失 缩小的animation
					// 3.被点击按钮放大后消失 透明度渐变 放大渐变的animation
					//composerButtonsShowHideButton.startAnimation(MyAnimations.getMiniAnimation(300));
					composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(-225, 0, 300));
					areButtonsShowing = !areButtonsShowing;
					smallIcon.startAnimation(MyAnimations.getMaxAnimation(400));
					for (int j = 0; j < composerButtonsWrapper.getChildCount(); j++) {
						if (j != position) {
							final ImageView smallIcon = (ImageView) composerButtonsWrapper.getChildAt(j);
							smallIcon.startAnimation(MyAnimations.getMiniAnimation(300));
						}
					}
				}
			});
		}
	}

}
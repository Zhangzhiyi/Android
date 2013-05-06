package com.pocketjourney.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.pocketjourney.tutorials.R;

public class ImageOnlyButton extends ImageButton {

	int imageResourceNotFocused, imageResourceFocused, imageResourcePressed;

	private boolean isButtonPressed;
	
	public ImageOnlyButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	public ImageOnlyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public ImageOnlyButton(Context context) {
		super(context);
		throw new RuntimeException("Valid image resource IDs must be passed to this class via the XML parameters: pj:resourceNotFocused & pj:resourceFocused.");
	}

	private void init(AttributeSet attrs) 
	{
		TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.ImageOnlyButton);
		//res/drawable-hdpi/help.png
        String notFocusedColorStr = a.getString(R.styleable.ImageOnlyButton_resourceNotFocused);
        //res/drawable-hdpi/help_focused.png
        String focusedColorStr = a.getString(R.styleable.ImageOnlyButton_resourceFocused);
        //res/drawable-hdpi/help_pressed.png
        String pressedColorStr = a.getString(R.styleable.ImageOnlyButton_resourcePressed);
        
        if (notFocusedColorStr != null && focusedColorStr != null && pressedColorStr != null) {
        	imageResourceFocused    = a.getResourceId(R.styleable.ImageOnlyButton_resourceFocused, -1);
        	Drawable drawable    = a.getDrawable(R.styleable.ImageOnlyButton_resourceFocused);
        	imageResourceNotFocused = a.getResourceId(R.styleable.ImageOnlyButton_resourceNotFocused, -1);
        	imageResourcePressed    = a.getResourceId(R.styleable.ImageOnlyButton_resourcePressed, -1);
        }
		
		
        if (imageResourceFocused == -1 || imageResourceNotFocused == -1 || imageResourcePressed == -1) {
    		throw new RuntimeException("Valid image resource IDs must be passed to this class via the XML parameters: pj:resourceNotFocused, pj:resourceFocused, & pj:resourcePressed.");
        }
		
	}
	
	/**
	 *  Capture mouse press events to update text state. 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent	event)
	{
		Log.d("TextOnlyButton",event.getAction()+"");
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			//  Request a redraw to update the button color
			isButtonPressed = true;
			invalidate();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			isButtonPressed = false;

			//  Requesting focus doesn't work for some reason.  If you find a solution to setting 
			//  the focus, please let me know so I can update the tutorial
		//	requestFocus();
			
			//  Request a redraw to update the button color
			invalidate();
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onDraw(Canvas canvas) {
		
		if (isButtonPressed) {
			setImageResource(imageResourcePressed);
		}else if (isFocused()) {
			//  Since this Button now has no background.  We must swap out the image to display 
			//	one that indicates it has focus.
			setImageResource(imageResourceFocused); 
		} else {
			setImageResource(imageResourceNotFocused);
		}
		super.onDraw(canvas);
	}
}

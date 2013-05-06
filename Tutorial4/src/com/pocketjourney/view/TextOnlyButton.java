package com.pocketjourney.view;



import com.pocketjourney.tutorials.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import android.widget.Button;


public class TextOnlyButton extends Button {

	private int notFocusedTextColor, focusedTextColor, pressedTextColor;
	
	private boolean isTextPressed;
	
	public TextOnlyButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}
	public TextOnlyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public TextOnlyButton(Context context) {
		super(context);
		
		throw new RuntimeException("Valid colors (e.g. #ffffff) must be passed to this class via the XML parameters: pj:textColorNotFocused & pj:textColorFocused.");
	}
	
	private void init(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.TextOnlyButton);
		String textColorNotFocused = a.getString(R.styleable.TextOnlyButton_textColorNotFocused);
		String textColorFocused = a.getString(R.styleable.TextOnlyButton_textColorFocused);
		String textColorPressed = a.getString(R.styleable.TextOnlyButton_textColorPressed);
		
        if (textColorNotFocused != null && textColorFocused != null && textColorPressed != null ) {
        	notFocusedTextColor = a.getColor(R.styleable.TextOnlyButton_textColorNotFocused, 0xFF000000);
        	focusedTextColor = a.getColor(R.styleable.TextOnlyButton_textColorFocused, 0xFF000000);
        	pressedTextColor = a.getColor(R.styleable.TextOnlyButton_textColorPressed, 0xFF000000);
        } else {
    		throw new RuntimeException("Valid colors (e.g. #ffffff) must be passed to this class via the XML parameters: pj:textColorNotFocused, pj:textColorFocused, & pj:textColorPressed.");
        }
	}
	
	public void onDrawBackground(Canvas	canvas) {
		//  Override this method & do nothing.  This prevents the parent.onDrawBackground(canvas)  
		//  from drawing the button's background.
	}

	/**
	 *  Capture mouse press events to update text state. 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent	event)
	{
		Log.d("TextOnlyButton",event.getAction()+"");
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			isTextPressed = true;
			
			//  Request a redraw to update the text color
			invalidate();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			isTextPressed = false;
			
			//  Requesting focus doesn't work for some reason.  If you find a solution to setting 
			//  the focus, please let me know so I can update the tutorial
			requestFocus();
			
			//  Request a redraw to update the text color
			invalidate();
		}
		return super.onTouchEvent(event);
	}

	
	@Override
	public void onDraw(Canvas canvas) {
		
		if (isTextPressed) {
			setTextColor(pressedTextColor);
		}else if (isFocused()) {
			//  Since this Button now has no background.  We adjust the text color to indicate focus.
			setTextColor(focusedTextColor);   
		} else {
			setTextColor(notFocusedTextColor);  
		}
		super.onDraw(canvas);
	}
}

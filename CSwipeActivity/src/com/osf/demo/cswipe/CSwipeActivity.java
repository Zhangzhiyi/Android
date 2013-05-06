package com.osf.demo.cswipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.osf.demo.cswipe.widget.CSwipe;

import java.util.ArrayList;

public class CSwipeActivity extends Activity {

    private static final String GESTURE_CSWIPE_LEFT_MARGIN = "cswipe_left_margin";
    private static final String GESTURE_CSWIPE_RIGHT_MARGIN = "cswipe_right_margin";

    private GestureLibrary mGestureLib;

    private View mContentView;
    private GestureOverlayView mGestureOverlayView;
    private CSwipe mCSwipe;

    private PopupWindow mCSwipePopupWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the content view
        mContentView = getLayoutInflater().inflate(R.layout.activity_cswipe, null);

        // Create a click listener which opens the web page
        final View.OnClickListener openUrlClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.KUrl))));
            }
        };

        // Set the click listener for all items
        mContentView.findViewById(R.id.image_logo).setOnClickListener(openUrlClickListener);
        mContentView.findViewById(R.id.text_explore).setOnClickListener(openUrlClickListener);
        mContentView.findViewById(R.id.image_book).setOnClickListener(openUrlClickListener);

        // Set formatted HTML text
        ((TextView) mContentView.findViewById(R.id.text_explore)).setText(Html.fromHtml(getString(R.string.KExploreLong)));

        // Create a gesture overlay view
        mGestureOverlayView = new GestureOverlayView(this);
        mGestureOverlayView.setGestureVisible(false);

        // Add content view to gesture overlay view
        mGestureOverlayView.addView(mContentView);

        // Register gestures listener
        mGestureOverlayView.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
                onGesture(gestureOverlayView, gesture);
            }
        });

        // Set gesture overlay view as content
        setContentView(mGestureOverlayView);

        // Load gestures library
        mGestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures_cswipe);
        if (!mGestureLib.load()) {
            // Show toast if gestures library could not be loaded
            Toast.makeText(this, R.string.KMsgErrLoadingGestures, Toast.LENGTH_SHORT).show();
        }
    }

    private void onGesture(GestureOverlayView gestureOverlayView, Gesture gesture) {
        // Get gesture predictions sorted by score
        final ArrayList<Prediction> gesturePredictions = mGestureLib.recognize(gesture);

        // Check if any gestures were found
        if (gesturePredictions.size() > 0) {
            // Select the gesture prediction with the highest score
            final Prediction prediction = gesturePredictions.get(0);

            if (prediction.score > 3D) {
                // Switch content from gesture overlay view to original content view
                mGestureOverlayView.removeView(mContentView);
                setContentView(mContentView);

                // Inflate the CSwipe control view
                final View cSwipePopupContentView = getLayoutInflater().inflate(R.layout.view_cswipe, null);
                mCSwipe = (CSwipe) cSwipePopupContentView.findViewById(R.id.cswipe);

                // Check the orientation of the CSwipe control based on the selected gesture prediction
                final String predictionName = prediction.name;
                CSwipe.Anchor cSwipeAnchor = CSwipe.Anchor.RIGHT;
                if (predictionName.equals(GESTURE_CSWIPE_LEFT_MARGIN)) { cSwipeAnchor = CSwipe.Anchor.LEFT; }
                else if (predictionName.equals(GESTURE_CSWIPE_RIGHT_MARGIN)) { cSwipeAnchor = CSwipe.Anchor.RIGHT; }

                // Set the CSwipe control anchor according to the selected gesture prediction
                mCSwipe.setAnchor(cSwipeAnchor);

                // Set the CSwipe control adapter
                mCSwipe.setAdapter(new CSwipeAdapter(new CSwipeItem[] {
                        new CSwipeItem("Share",         0xFF000000, 0xFF9D0001, BitmapFactory.decodeResource(getResources(), R.drawable.arrow_black),BitmapFactory.decodeResource(getResources(), R.drawable.arrow_red)),
                        new CSwipeItem("Edit",          0xFF000000, 0xFF9D0001, BitmapFactory.decodeResource(getResources(), R.drawable.discuss_black), BitmapFactory.decodeResource(getResources(), R.drawable.discuss_red)),
                        new CSwipeItem("Delete",        0xFF000000, 0xFF9D0001, BitmapFactory.decodeResource(getResources(), R.drawable.share_black), BitmapFactory.decodeResource(getResources(), R.drawable.share_red)),
                        new CSwipeItem("Preferences",   0xFF000000, 0xFF9D0001, BitmapFactory.decodeResource(getResources(), R.drawable.star_black), BitmapFactory.decodeResource(getResources(), R.drawable.start_red)),
                }));

                // Register for CSwipe control items selection
                mCSwipe.setOnItemSelectedListener(new CSwipe.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(CSwipe.Item item, int itemIndex) {
                        // Show an alert dialog
                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CSwipeActivity.this);
                        dialogBuilder.setTitle(null);
                        dialogBuilder.setMessage(getString(R.string.KExploreShort));
                        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                if (mCSwipePopupWindow != null) {
                                    mCSwipePopupWindow.dismiss();
                                }
                            }
                        });
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.show();
                    }
                });

                // Create a popup window to host the CSwipe control
                mCSwipePopupWindow = new PopupWindow(cSwipePopupContentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                mCSwipePopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));

                // Register for CSwipe control popup window dismissal
                mCSwipePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mCSwipePopupWindow = null;

                        // Switch content from original content view to gesture overlay view
                        setContentView(mGestureOverlayView);
                        mGestureOverlayView.addView(mContentView);
                    }
                });

                // Show the CSwipe control popup window as close as possible to the gesture bounding rectangle
                final RectF gestureBoundingRect = gesture.getBoundingBox();
                mCSwipePopupWindow.showAtLocation(mContentView, Gravity.NO_GRAVITY, (int) gestureBoundingRect.left, (int) gestureBoundingRect.top);
            }
        }
    }

    private static final class CSwipeItem implements CSwipe.Item {

        private final String mText;
        private final int mColor;
        private final int mPressedColor;
        private final Bitmap mIcon;
        private final Bitmap mPressedIcon;

        public CSwipeItem(final String text, final int color, final int pressedColor, final Bitmap icon, final Bitmap pressedIcon) {
            this.mText = text;
            this.mColor = color;
            this.mPressedColor = pressedColor;
            this.mIcon = icon;
            this.mPressedIcon = pressedIcon;
        }

        @Override
        public String getText() { return mText; }
        @Override
        public int getColor() { return mColor; }
        @Override
        public int getPressedColor() { return mPressedColor; }
        @Override
        public Bitmap getIcon() { return mIcon; }
        @Override
        public Bitmap getPressedIcon() { return mPressedIcon; }

    }

    private static final class CSwipeAdapter implements CSwipe.Adapter {

        private final CSwipeItem[] mItems;

        public CSwipeAdapter(final CSwipeItem[] items) {
            this.mItems = items;
        }

        @Override
        public int getItemsCount() {
            return (mItems != null ? mItems.length : 0);
        }

        @Override
        public CSwipe.Item getItem(final int itemIndex) {
            return mItems[itemIndex];
        }
    }

}

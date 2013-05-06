package com.osf.demo.cswipe.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import com.osf.demo.cswipe.R;

/**
 * User: Cosmin Radu
 * Date: 1/22/13
 * Time: 9:44 AM
 */
public class CSwipe extends View {

    public enum Anchor {

        TOP     (0,     Math.PI * 2,    Gravity.TOP | Gravity.CENTER_HORIZONTAL),
        RIGHT   (90,    Math.PI * 1.5,  Gravity.RIGHT | Gravity.CENTER_VERTICAL),
        BOTTOM  (180,   Math.PI,        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL),
        LEFT    (270,   Math.PI * 0.5,  Gravity.LEFT | Gravity.CENTER_VERTICAL);

        /**
         * Arc start in degrees (0º corresponds to 3 o'clock on a watch and increases clockwise)
         */
        private final int arcStartD;

        /**
         * Arc start in radians (0 corresponds to 3 o'clock on a watch and increases counter-clockwise)
         */
        private final double arcStartR;

        /**
         * The gravity preferred by each anchor mode
         */
        private final int preferredGravity;

        Anchor(final int arcStartD, final double arcStartR, final int preferredGravity) {
            this.arcStartD = arcStartD;
            this.arcStartR = arcStartR;
            this.preferredGravity = preferredGravity;
        }

        private boolean matchesAngle(final double angle) {
            switch (this) {
                case RIGHT: return Math.signum(Math.cos(angle)) <= 0D;
                case LEFT: return Math.signum(Math.cos(angle)) >= 0D;
                case TOP: return Math.signum(Math.sin(angle)) <= 0D;
                case BOTTOM: return Math.signum(Math.sin(angle)) >= 0D;
                default: return false;
            }
        }

    }

    private static final double TWO_PI = 2 * Math.PI;

    private static final int DEFAULT_OUTER_RADIUS_DP = 100;
    private static final int DEFAULT_INNER_RADIUS_DP = 35;

    private static final Anchor DEFAULT_ANCHOR = Anchor.RIGHT;

    private static final int SUGGESTED_MINIMUM_WIDTH_DP = DEFAULT_OUTER_RADIUS_DP * 2;
    private static final int SUGGESTED_MINIMUM_HEIGHT_DP = DEFAULT_OUTER_RADIUS_DP * 2;

    private static final int TRACKING_POINTER_INVALID_ID = Integer.MIN_VALUE;

    private Bitmap mControlCacheBitmap;
    private Bitmap mDrawBitmap;

    private Paint mFillPaint;
    private Paint mStrokePaint;
    private Paint mErasePaint;

    private Anchor mAnchor;
    private int mInnerArcRadius;
    private int mOuterArcRadius;
    private int mOuterIconsArcRadius;
    private int mGravity;

    private RectF mDrawableRect;
    private RectF mContentRect;
    private RectF mContentRectRelative;
    private PointF mOrigin;
    private PointF mOriginRelative;
    private RectF mOuterArcOvalRect;
    private RectF mInnerArcOvalRect;

    private float mItemArcAngleD;
    private double mItemArcAngleR;

    private int mMaxItemIconWidth;
    private int mMaxItemIconHeight;

    private int mTrackingPointerId;
    private PointF mTrackingPointerPosition;
    private double mTrackingPointerAngleR;

    private Adapter mAdapter;

    private int mItemsCount;
    private ItemModel[] mItemsModel;
    private ItemModel mSelectedItem;

    private OnItemSelectedListener mOnItemSelectedListener;

    public CSwipe(Context context) {
        this(context, null);
    }

    public CSwipe(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CSwipe(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CSwipe);

        mInnerArcRadius = attributes.getDimensionPixelSize(R.styleable.CSwipe_innerArcRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_INNER_RADIUS_DP, getResources().getDisplayMetrics()));
        mOuterArcRadius = attributes.getDimensionPixelSize(R.styleable.CSwipe_outerArcRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_OUTER_RADIUS_DP, getResources().getDisplayMetrics()));

        mAnchor = Anchor.values()[attributes.getInt(R.styleable.CSwipe_anchor, DEFAULT_ANCHOR.ordinal())];
        mGravity = attributes.getInt(R.styleable.CSwipe_gravity, mAnchor.preferredGravity);

        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFillPaint.setStyle(Paint.Style.FILL);

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);

        mErasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        initialize();
    }

    private final void initialize() {
        // Save items count
        mItemsCount = (mAdapter != null ? mAdapter.getItemsCount() : 0);

        // Create array of item models
        mItemsModel = new ItemModel[mItemsCount];

        // Compute arc angle in degrees for one item
        mItemArcAngleD = (mItemsCount > 0 ? 180F / mItemsCount : 0F);

        // Compute arc angle in radians for one item
        mItemArcAngleR = (mItemsCount > 0 ? Math.PI / mItemsCount : 0D);

        mMaxItemIconWidth = 0;
        mMaxItemIconHeight = 0;

        double iAngleR = mAnchor.arcStartR;
        float iAngleD = mAnchor.arcStartD;
        for (int i = 0 ; i < mItemsCount ; ++i) {
            // Create item model from adapter item at given index
            ItemModel itemModel = new ItemModel(mAdapter.getItem(i), iAngleR, iAngleR - mItemArcAngleR, iAngleD, iAngleD + mItemArcAngleD);

            // Save item model
            mItemsModel[i] = itemModel;

            // Decrease degrees angle iterator
            iAngleD += mItemArcAngleD;

            // Decrease radians angle iterator
            iAngleR -= mItemArcAngleR;

            // Compute the maximum item icon width and height (take into account both pressed and unpressed icons)
            Bitmap itemIconBitmap = itemModel.getIcon();
            if (itemIconBitmap != null) {
                mMaxItemIconWidth = Math.max(mMaxItemIconWidth, itemIconBitmap.getWidth());
                mMaxItemIconHeight = Math.max(mMaxItemIconHeight, itemIconBitmap.getHeight());
            }

            Bitmap itemIconPressedBitmap = itemModel.getPressedIcon();
            if (itemIconPressedBitmap != null) {
                mMaxItemIconWidth = Math.max(mMaxItemIconWidth, itemIconPressedBitmap.getWidth());
                mMaxItemIconHeight = Math.max(mMaxItemIconHeight, itemIconPressedBitmap.getHeight());
            }
        }

        // Compute the radius used to determine the center point where icons will be drawn
        mOuterIconsArcRadius = mOuterArcRadius + (int) (Math.sqrt(Math.pow(mMaxItemIconWidth, 2) + Math.pow(mMaxItemIconHeight, 2)) / 2);

        // Compute drawable rect (view rect excluding padding)
        mDrawableRect = new RectF(
                getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());

        // Compute content rect
        switch (mAnchor) {
            case LEFT: mContentRect = new RectF(0, 0, mOuterArcRadius + mMaxItemIconWidth, (mOuterArcRadius + mMaxItemIconHeight) * 2); break;
            case TOP: mContentRect = new RectF(0, 0, (mOuterArcRadius + mMaxItemIconWidth) * 2, mOuterArcRadius + mMaxItemIconHeight); break;
            case RIGHT: mContentRect = new RectF(0, 0, mOuterArcRadius + mMaxItemIconWidth, (mOuterArcRadius + mMaxItemIconHeight) * 2); break;
            case BOTTOM: mContentRect = new RectF(0, 0, (mOuterArcRadius + mMaxItemIconWidth) * 2, mOuterArcRadius + mMaxItemIconHeight); break;
        }

        // Compute arc origin relative to the content rect
        switch (mAnchor) {
            case LEFT:      mOrigin = new PointF(0F, mContentRect.height() / 2); break;
            case TOP:       mOrigin = new PointF(mContentRect.width() / 2, 0F); break;
            case RIGHT:     mOrigin = new PointF(mContentRect.right, mContentRect.height() / 2); break;
            case BOTTOM:    mOrigin = new PointF(mContentRect.width() / 2, mContentRect.bottom); break;
        }

        mOriginRelative = new PointF(mOrigin.x, mOrigin.y);
        mContentRectRelative = new RectF(mContentRect);

        // Compute X and Y offsets for the content rect within entire view rect

        float dx = mDrawableRect.left;
        float dy = mDrawableRect.top;

        if (mGravity != 0) {
            final int horizontalGravity = mGravity & Gravity.HORIZONTAL_GRAVITY_MASK;

            if (horizontalGravity == Gravity.LEFT) { dx = mDrawableRect.left; }
            else if (horizontalGravity == Gravity.RIGHT) { dx = mDrawableRect.right - mContentRect.right; }
            else if (horizontalGravity == Gravity.CENTER_HORIZONTAL) { dx = mDrawableRect.left + (mDrawableRect.width() - mContentRect.width()) / 2; }

            final int verticalGravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;

            if (verticalGravity == Gravity.TOP) { dy = mDrawableRect.top; }
            else if (verticalGravity == Gravity.BOTTOM) { dy = mDrawableRect.bottom - mContentRect.bottom; }
            else if (verticalGravity == Gravity.CENTER_VERTICAL) { dy = mDrawableRect.top + (mDrawableRect.height() - mContentRect.height()) / 2; }
        }

        // Save arc origin relative to the view rect
        mOriginRelative.offset(dx, dy);

        // Save content rect relative to the view rect
        mContentRectRelative.offset(dx, dy);

        // Compute inner arc bounding rect
        mInnerArcOvalRect = new RectF(
                mOrigin.x - mInnerArcRadius,
                mOrigin.y - mInnerArcRadius,
                mOrigin.x + mInnerArcRadius,
                mOrigin.y + mInnerArcRadius
        );

        // Compute outer arc bounding rect
        mOuterArcOvalRect = new RectF(
                mOrigin.x - mOuterArcRadius,
                mOrigin.y - mOuterArcRadius,
                mOrigin.x + mOuterArcRadius,
                mOrigin.y + mOuterArcRadius
        );

        // Create a bitmap to fit the entire content rect where to draw static part of the control
        mControlCacheBitmap = Bitmap.createBitmap((int) mContentRect.width(), (int) mContentRect.height(), Bitmap.Config.ARGB_8888);

        // Create a bitmap to fit the entire content rect where to draw the control
        mDrawBitmap = Bitmap.createBitmap((int) mContentRect.width(), (int) mContentRect.height(), Bitmap.Config.ARGB_8888);

        // Draw and cache the static part of the control
        drawBase(new Canvas(mControlCacheBitmap));
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initialize();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec);

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        int measuredWidth;
        int measuredHeight;

        // Compute width
        switch (widthMode) {
            case MeasureSpec.EXACTLY: measuredWidth = width; break;
            case MeasureSpec.AT_MOST: measuredWidth = Math.min(getPaddingLeft() + (int) mContentRect.width() + getPaddingRight(), width); break;
            default /* MeasureSpec.UNSPECIFIED */: measuredWidth = getPaddingLeft() + (int) mContentRect.width() + getPaddingRight(); break;
        }

        // Compute height
        switch (heightMode) {
            case MeasureSpec.EXACTLY: measuredHeight = height; break;
            case MeasureSpec.AT_MOST: measuredHeight = Math.min(getPaddingTop() + (int) mContentRect.height() + getPaddingBottom(), height); break;
            default /* MeasureSpec.UNSPECIFIED */: measuredHeight = getPaddingTop() + (int) mContentRect.height() + getPaddingBottom(); break;
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int eventAction = event.getAction();

        switch (eventAction & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: { // First pointer went down
                // Save the ID of the pointer that we will be tracking
                mTrackingPointerId = event.getPointerId(0);
                // Handle tracking pointer down
                onTrackingPointerDown(event.getX(), event.getY());

                break;
            }
            case MotionEvent.ACTION_MOVE: { // One pointer moved
                // Get pointer index for the pointer we are tracking
                final int trackingPointerIndex = event.findPointerIndex(mTrackingPointerId);
                // Handle tracking pointer moved
                onTrackingPointerMoved(event.getX(trackingPointerIndex), event.getY(trackingPointerIndex));

                break;
            }
            case MotionEvent.ACTION_POINTER_UP: { // One pointer went up
                // Get the index of the pointer subject to this event
                final int eventPointerIndex = (eventAction & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                // Get the id of the pointer subject to this event
                final int eventPointerId = event.getPointerId(eventPointerIndex);
                // Check if event concerns the pointer being tracked
                if (eventPointerId == mTrackingPointerId) {
                    // Pointer being tracked went up. Switch to nearest available pointer
                    final int newTrackingPointerIndex = (eventPointerIndex == 0 ? 1 : 0);
                    // Update tracked pointer id
                    mTrackingPointerId = event.getPointerId(newTrackingPointerIndex);
                    // Handle tracking pointer moved
                    onTrackingPointerMoved(event.getX(newTrackingPointerIndex), event.getY(newTrackingPointerIndex));
                }

                break;
            }
            case MotionEvent.ACTION_UP: { // Last pointer went up
                // Invalidate tracking pointer ID
                mTrackingPointerId = TRACKING_POINTER_INVALID_ID;
                // Handle tracking pointer up
                onTrackingPointerUp(event.getX(), event.getY());
                break;
            }
        }

        return true;
    }

    private final void onTrackingPointerDown(final float trackingX, final float trackingY) {
        // Save position of tracked pointer
        mTrackingPointerPosition = new PointF(trackingX, trackingY);

        // Compute angle between tracking point and origin
        mTrackingPointerAngleR = (TWO_PI + Math.atan2(
                mOriginRelative.y - mTrackingPointerPosition.y,
                mTrackingPointerPosition.x - mOriginRelative.x
        )) % TWO_PI;

        // Compute distance between tracking point and origin
        final double pointerDistanceToOrigin = Math.sqrt(Math.pow(Math.abs(mOriginRelative.x - mTrackingPointerPosition.x), 2) +
                Math.pow(Math.abs(mOriginRelative.y - mTrackingPointerPosition.y), 2));

        // If tracked pointer is between inner arc radius and outer arc radius, check if angle overlaps an item
        if (pointerDistanceToOrigin <= mOuterArcRadius && pointerDistanceToOrigin >= mInnerArcRadius) {
            final int selectedItemIndex = getItemModelIndexForAngleR(mTrackingPointerAngleR);
            mSelectedItem = selectedItemIndex != Integer.MIN_VALUE ? mItemsModel[selectedItemIndex] : null;
        } else {
            mSelectedItem = null;
        }

        invalidate();
    }

    private final void onTrackingPointerMoved(final float trackingX, final float trackingY) {
        // Save position of tracked pointer
        mTrackingPointerPosition.set(trackingX, trackingY);

        // Compute angle between tracking point and origin
        mTrackingPointerAngleR = (TWO_PI + Math.atan2(
                mOriginRelative.y - mTrackingPointerPosition.y,
                mTrackingPointerPosition.x - mOriginRelative.x
        )) % TWO_PI;

        // Compute distance between tracking point and origin
        final double pointerDistanceToOrigin = Math.sqrt(Math.pow(Math.abs(mOriginRelative.x - mTrackingPointerPosition.x), 2) +
                Math.pow(Math.abs(mOriginRelative.y - mTrackingPointerPosition.y), 2));

        // If tracked pointer is between inner arc radius and outer arc radius, check if angle overlaps an item
        if (pointerDistanceToOrigin <= mOuterArcRadius && pointerDistanceToOrigin >= mInnerArcRadius) {
            final int selectedItemIndex = getItemModelIndexForAngleR(mTrackingPointerAngleR);
            mSelectedItem = selectedItemIndex != Integer.MIN_VALUE ? mItemsModel[selectedItemIndex] : null;
        } else {
            mSelectedItem = null;
        }

        invalidate();
    }

    private final void onTrackingPointerUp(final float trackingX, final float trackingY) {
        // Save position of tracked pointer
        mTrackingPointerPosition.set(trackingX, trackingY);

        // Compute angle between tracking point and origin
        mTrackingPointerAngleR = (TWO_PI + Math.atan2(
                mOriginRelative.y - mTrackingPointerPosition.y,
                mTrackingPointerPosition.x - mOriginRelative.x
        )) % TWO_PI;

        // Compute distance between tracking point and origin
        final double pointerDistanceToOrigin = Math.sqrt(Math.pow(Math.abs(mOriginRelative.x - mTrackingPointerPosition.x), 2) +
                Math.pow(Math.abs(mOriginRelative.y - mTrackingPointerPosition.y), 2));

        // If tracked pointer is between inner arc radius and outer arc radius, check if angle overlaps an item
        if (pointerDistanceToOrigin <= mOuterArcRadius && pointerDistanceToOrigin >= mInnerArcRadius) {
            final int selectedItemIndex = getItemModelIndexForAngleR(mTrackingPointerAngleR);
            mSelectedItem = selectedItemIndex != Integer.MIN_VALUE ? mItemsModel[selectedItemIndex] : null;

            // If there is any selection listener, fire notification
            if (mOnItemSelectedListener != null && mSelectedItem != null) {
                mOnItemSelectedListener.onItemSelected(mSelectedItem, selectedItemIndex);
            }
        } else {
            mSelectedItem = null;
        }

        // Clear tracking data
        mTrackingPointerId = TRACKING_POINTER_INVALID_ID;
        mTrackingPointerPosition = null;
        mTrackingPointerAngleR = Double.NaN;

        mSelectedItem = null;

        invalidate();
    }

    public final Anchor getAnchor() {
        return mAnchor;
    }

    public void setAnchor(final Anchor anchor) {
        if (mAnchor != anchor) {
            mAnchor = (anchor != null ? anchor : DEFAULT_ANCHOR);
            initialize();
            requestLayout();
        }
    }

    public final int getGravity() {
        return mGravity;
    }

    public void setGravity(final int gravity) {
        if (mGravity != gravity) {
            mGravity = gravity;
            initialize();
        }
    }

    public void setAdapter(final Adapter adapter) {
        mAdapter = adapter;
        initialize();
        invalidate();
    }

    public void setOnItemSelectedListener(final OnItemSelectedListener listener) {
        this.mOnItemSelectedListener = listener;
    }


    private final int getItemModelIndexForAngleR(final double angleR) {
        // Filter out angles initially by anchor
        if (!mAnchor.matchesAngle(angleR)) {
            return Integer.MIN_VALUE;
        }

        // Compute angle sine an cosine value
        final double sinAngleR = Math.sin(angleR);
        final double cosAngleR = Math.cos(angleR);

        // Iterate through all items and find the one where the computed sine and cosine are within the item's bounds

        ItemModel itemModel;

        double itemArcStartSin;
        double itemArcEndSin;
        double itemArcStartCos;
        double itemArcEndCos;
        for (int i = 0 ; i < mItemsCount ; ++i) {
            itemModel = mItemsModel[i];

            itemArcStartSin = itemModel.getArcStartSin();
            itemArcEndSin = itemModel.getArcEndSin();
            itemArcStartCos = itemModel.getArcStartCos();
            itemArcEndCos = itemModel.getArcEndCos();

            Math.min(itemArcStartSin, itemArcEndSin);
            Math.max(itemArcStartSin, itemArcEndSin);

            Math.min(itemArcStartCos, itemArcEndCos);
            Math.max(itemArcStartCos, itemArcEndCos);

            if (sinAngleR >= Math.min(itemArcStartSin, itemArcEndSin) && sinAngleR <= Math.max(itemArcStartSin, itemArcEndSin)
                    && cosAngleR >= Math.min(itemArcStartCos, itemArcEndCos) && cosAngleR <= Math.max(itemArcStartCos, itemArcEndCos)) {
                return i;
            }
        }

        return Integer.MIN_VALUE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Set clipping to the drawable rect
        canvas.clipRect(mDrawableRect);

        // Create a drawing canvas wrapping the drawing bitmap
        final Canvas drawCanvas = new Canvas(mDrawBitmap);

        // Clear drawing canvas
        drawCanvas.drawRect(0, 0, drawCanvas.getWidth(), drawCanvas.getHeight(), mErasePaint);

        // Draw the static control cache into the drawing bitmap
        drawCanvas.drawBitmap(mControlCacheBitmap, 0, 0, null);

        // If an item is selected, draw the pressed item into the drawing bitmap
        if (mSelectedItem != null) {
            drawPressed(drawCanvas);
        }

        // Erase required areas from the drawing bitmap
        drawErase(drawCanvas);

        // Copy the drawing bitmap into the view's canvas
        canvas.drawBitmap(mDrawBitmap, mContentRectRelative.left, mContentRectRelative.top, null);
    }

    private void drawBase(Canvas canvas) {
        if (mItemsCount > 0) {
            final int iItemArcAngleD = (int) Math.ceil(mItemArcAngleD) + 1;

            // Iterate through all the items and draw them

            ItemModel item;
            for (int i = 0 ; i < mItemsCount ; ++i) {
                // Get item at index
                item = mItemsModel[i];

                // Set paint color to the item's unpressed color
                mFillPaint.setColor(item.getColor());

                // Draw the outer arc fill for this item
                canvas.drawArc(mOuterArcOvalRect, (int) Math.floor(item.getArcStartD()), iItemArcAngleD, true, mFillPaint);

                // Compute the angle for the middle of the item arc
                final double itemMiddleAngleR = item.getArcStartR() - (mItemArcAngleR * 0.5);

                // Compute distance from item bullet center position to origin
                final int bulletCenterToOrigin = mOuterArcRadius - (mOuterArcRadius - mInnerArcRadius) / 2;

                // Compute item bullet center position
                final PointF bulletCenter = new PointF(
                        mOrigin.x + (float) (bulletCenterToOrigin * Math.cos(itemMiddleAngleR)),
                        mOrigin.y - (float) (bulletCenterToOrigin * Math.sin(itemMiddleAngleR))
                );

                // Set paint color to the item's pressed color
                mFillPaint.setColor(item.getPressedColor());

                // Draw the bullet
                canvas.drawCircle(bulletCenter.x, bulletCenter.y, 10F, mFillPaint);

                // Draw the unpressed item icon
                final Bitmap itemIconBitmap = item.getIcon();
                if (itemIconBitmap != null) {
                    // Compute icon center position
                    final PointF iconCenter = new PointF(
                            mOriginRelative.x + (float) (mOuterIconsArcRadius * Math.cos(itemMiddleAngleR)),
                            mOriginRelative.y - (float) (mOuterIconsArcRadius * Math.sin(itemMiddleAngleR))
                    );

                    // Draw the icon
                    canvas.drawBitmap(itemIconBitmap, iconCenter.x - itemIconBitmap.getWidth() / 2, iconCenter.y - itemIconBitmap.getHeight() / 2, null);
                }
            }
        }
    }

    private void drawPressed(Canvas canvas) {
        final int iItemArcAngleD = (int) Math.ceil(mItemArcAngleD) + 1;

        // Set paint color to the item's pressed color
        mFillPaint.setColor(mSelectedItem.getPressedColor());

        // Draw the outer arc fill for this item
        canvas.drawArc(mOuterArcOvalRect, (int) Math.floor(mSelectedItem.getArcStartD()), iItemArcAngleD, true, mFillPaint);

        // Compute the angle for the middle of the item arc
        final double itemMiddleAngleR = mSelectedItem.getArcStartR() - (mItemArcAngleR * 0.5);

        // Compute distance from item bullet center position to origin
        final int bulletCenterToOrigin = mOuterArcRadius - (mOuterArcRadius - mInnerArcRadius) / 2;

        // Compute item bullet center position
        final PointF bulletCenter = new PointF(
                mOrigin.x + (float) (bulletCenterToOrigin * Math.cos(itemMiddleAngleR)),
                mOrigin.y - (float) (bulletCenterToOrigin * Math.sin(itemMiddleAngleR))
        );

        // Set paint color to the item's pressed color
        mFillPaint.setColor(mSelectedItem.getColor());

        // Draw the bullet
        canvas.drawCircle(bulletCenter.x, bulletCenter.y, 10F, mFillPaint);

        // Draw the pressed item icon
        final Bitmap itemPressedIconBitmap = mSelectedItem.getPressedIcon();
        if (itemPressedIconBitmap != null) {
            // Compute icon center position
            final PointF iconCenter = new PointF(
                    mOriginRelative.x + (float) (mOuterIconsArcRadius * Math.cos(itemMiddleAngleR)),
                    mOriginRelative.y - (float) (mOuterIconsArcRadius * Math.sin(itemMiddleAngleR))
            );

            // Draw the icon
            canvas.drawBitmap(itemPressedIconBitmap, iconCenter.x - itemPressedIconBitmap.getWidth() / 2, iconCenter.y - itemPressedIconBitmap.getHeight() / 2, null);
        }
    }

    private void drawErase(Canvas canvas) {
        if (mItemsCount > 0) {
            mErasePaint.setStyle(Paint.Style.STROKE);
            mErasePaint.setStrokeWidth(6F);

            // Draw item separator lines (by clearing the pixels)
            double arcStartR;
            for (int i = 0 ; i < mItemsCount ; ++i) {
                if (i > 0) {
                    arcStartR = mItemsModel[i].getArcStartR();
                    canvas.drawLine(
                            mOrigin.x,
                            mOrigin.y,
                            mOrigin.x + (float) (mOuterArcRadius * Math.cos(arcStartR)),
                            mOrigin.y - (float) (mOuterArcRadius * Math.sin(arcStartR)),
                            mErasePaint);
                }
            }
        }

        // Erase the inner arc
        mErasePaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(mInnerArcOvalRect, mAnchor.arcStartD, 180, true, mErasePaint);
    }

    public interface OnItemSelectedListener {

        void onItemSelected(Item item, int itemIndex);

    }

    public interface Item {

        String getText();
        int getColor();
        int getPressedColor();
        Bitmap getIcon();
        Bitmap getPressedIcon();

    }

    public interface Adapter {

        int getItemsCount();
        Item getItem(int itemIndex);

    }

    private static final class ItemModel implements Item {

        private final Item mItem;

        private final double mArcStartR;
        private final double mArcEndR;

        private final float mArcStartD;
        private final float mArcEndD;

        private final double mArcStartSin;
        private final double mArcEndSin;

        private final double mArcStartCos;
        private final double mArcEndCos;

        ItemModel(final Item item, final double arcStartR, final double arcEndR, final float arcStartD, final float arcEndD) {
            this.mItem = item;

            this.mArcStartR = arcStartR;
            this.mArcEndR = arcEndR;

            this.mArcStartD = arcStartD;
            this.mArcEndD = arcEndD;

            this.mArcStartSin = Math.sin(mArcStartR);
            this.mArcStartCos = Math.cos(mArcStartR);

            this.mArcEndSin = Math.sin(mArcEndR);
            this.mArcEndCos = Math.cos(mArcEndR);
        }

        @Override
        public String getText() { return mItem.getText(); }
        @Override
        public int getColor() { return mItem.getColor(); }
        @Override
        public int getPressedColor() { return mItem.getPressedColor(); }
        @Override
        public Bitmap getIcon() { return mItem.getIcon(); }
        @Override
        public Bitmap getPressedIcon() { return mItem.getPressedIcon(); }

        public double getArcStartR() { return mArcStartR; }
        public double getArcEndR() { return mArcEndR; }
        public float getArcStartD() { return mArcStartD; }
        public float getArcEndD() { return mArcEndD; }
        public double getArcStartSin() { return mArcStartSin; }
        public double getArcEndSin() { return mArcEndSin; }
        public double getArcStartCos() { return mArcStartCos; }
        public double getArcEndCos() { return mArcEndCos; }

    }

}

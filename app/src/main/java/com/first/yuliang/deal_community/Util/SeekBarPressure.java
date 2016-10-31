package com.first.yuliang.deal_community.Util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.first.yuliang.deal_community.R;

import java.math.BigDecimal;

public class SeekBarPressure extends View {
    private static final String TAG = "SeekBarPressure";

    private static final int CLICK_ON_LOW = 1;

    private static final int CLICK_ON_HIGH = 2;

    private static final int CLICK_IN_LOW_AREA = 3;

    private static final int CLICK_IN_HIGH_AREA = 4;

    private static final int CLICK_OUT_AREA = 5;

    private static final int CLICK_INVAILD = 0;

    /*
     * private static final int[] PRESSED_STATE_SET = {
     * android.R.attr.state_focused, android.R.attr.state_pressed,
     * android.R.attr.state_selected, android.R.attr.state_window_focused, };
     */

    private static final int[] STATE_NORMAL = {};

    private static final int[] STATE_PRESSED = {
            android.R.attr.state_pressed, android.R.attr.state_window_focused,
    };

    private Drawable mScrollBarBgNormal;

    private Drawable mScrollBarProgress;

    private Drawable mThumbLow;

    private Drawable mThumbHigh;

    private int mScollBarWidth;

    private int mScollBarHeight;

    private int mThumbWidth;

    private int mThumbHeight;

    private int mHalfScollBarHeight;

    private int mHalfThumbHeight;

    private int mOffsetLow = 0;

    private int mOffsetHigh = 0;

    private int mDistance = 0;

    private float mMin = 0;

    private float mMax = 0;

    private int mDuration = 0;

    private int mFlag = CLICK_INVAILD;

    private OnSeekBarChangeListener mBarChangeListener;

    public SeekBarPressure(Context context) {
        this(context, null);
    }

    public SeekBarPressure(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekBarPressure(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBarPressure, defStyle,
                0);

        mMin = a.getFloat(R.styleable.SeekBarPressure_minValue, mMin);
        mMax = a.getFloat(R.styleable.SeekBarPressure_maxValue, mMax);
        mScollBarWidth = a.getLayoutDimension(R.styleable.SeekBarPressure_width, "layout_width");
        mScollBarHeight = a.getLayoutDimension(R.styleable.SeekBarPressure_height, "layout_height");

        mDistance = mScollBarWidth - mThumbWidth;

        mDuration = (int)Math.rint(a.getFloat(R.styleable.SeekBarPressure_duration, mDuration)
                * mDistance / (mMax - mMin));

        if (mMin == 0) {
            throw new RuntimeException(a.getPositionDescription()
                    + ": You must supply a minValue attribute.");
        }
        if (mMax == 0) {
            throw new RuntimeException(a.getPositionDescription()
                    + ": You must supply a maxValue attribute.");
        }
        if (mMin > mMax) {
            throw new RuntimeException(a.getPositionDescription()
                    + ": The minValue attribute must be smaller than the maxValue attribute.");
        }
        if (mDuration == 0) {
            throw new RuntimeException(a.getPositionDescription()
                    + ": You must supply a duration attribute.");
        }

        if (mScollBarWidth == 0 || mScollBarWidth == -1 || mScollBarWidth == -2) {
            throw new RuntimeException(a.getPositionDescription()
                    + ": You must supply a width attribute.");
        }
        if (mScollBarHeight == 0 || mScollBarHeight == -1 || mScollBarHeight == -2) {
            throw new RuntimeException(a.getPositionDescription()
                    + ": You must supply a height attribute.");
        }

        a.recycle();

//        TyreLog.d(TAG, "Constructor-->mMinDuration: " + mDuration + "  mScollBarWidth: "
//                + mScollBarWidth + "  mScollBarHeight: " + mScollBarHeight + " mDistance: "
//                + mDistance + " mMax: " + mMax);

        // mOffsetHigh = mOffsetLow + mDuration;
        // TyreLog.d(TAG, "constructor-->mOffsetHigh: " + mOffsetHigh +
        // "  mOffsetLow: " + mOffsetLow
        // + "  mDuration: " + mDuration);
    }

    public static double formatDouble(double pDouble) {
        BigDecimal bd = new BigDecimal(pDouble);
        BigDecimal bd1 = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
        pDouble = bd1.doubleValue();
        return pDouble;
    }

    public void init(Context context) {
        Resources resources = getResources();

        mScrollBarBgNormal = resources.getDrawable(R.drawable.seekbarpressure_bg_progress);
        mScrollBarProgress = resources.getDrawable(R.drawable.seekbarpressure_bg_normal);
        mThumbLow = resources.getDrawable(R.drawable.seekbarpressure_thumb);
        mThumbHigh = resources.getDrawable(R.drawable.seekbarpressure_thumb);

        // setDrawableState(mThumbLow);
        // setDrawableState(mThumbHigh);
        mThumbLow.setState(STATE_NORMAL);
        mThumbHigh.setState(STATE_NORMAL);

        // mScollBarWidth = mScrollBarBgNormal.getIntrinsicWidth();
        // mScollBarHeight = mScrollBarBgNormal.getIntrinsicHeight();

        mThumbWidth = mThumbLow.getIntrinsicWidth();
        mThumbHeight = mThumbLow.getIntrinsicHeight();
//        TyreLog.d(TAG, "init-->mThumbWidth: " + mThumbWidth + "  mThumbHeight: " + mThumbHeight);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
         * TyreLog.d(TAG, "widthMeasureSpec: " + getDefaultSize(mScollBarWidth,
         * widthMeasureSpec) + " heightMeasureSpec" +
         * getDefaultSize(mScollBarHeight, heightMeasureSpec));
         */
        int width = mScollBarWidth;
        int height = mScollBarHeight;
        setMeasuredDimension(width, height);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        TyreLog.d(TAG, "changed: " + changed + "l:" + l + "t:" + t + "r:" + r + "b:" + b);
        // mScollBarWidth = r - l;
        // mScollBarHeight = b - t;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mHalfScollBarHeight = mScollBarHeight >> 1;
        mHalfThumbHeight = mThumbHeight >> 1;

        mScrollBarBgNormal.setBounds(mHalfScollBarHeight, mHalfScollBarHeight-15, mScollBarWidth-mHalfScollBarHeight, mHalfScollBarHeight+10);
        mScrollBarBgNormal.draw(canvas);

        mThumbLow.setBounds(mOffsetLow, mHalfScollBarHeight - mHalfThumbHeight, mOffsetLow
                + mThumbWidth, mHalfScollBarHeight + mHalfThumbHeight);
        mThumbLow.draw(canvas);

        mScrollBarProgress.setBounds(mOffsetLow + mThumbWidth - 20, mHalfScollBarHeight-15, mOffsetHigh + 20,
                mHalfScollBarHeight+10);
        mScrollBarProgress.draw(canvas);

        mThumbHigh.setBounds(mOffsetHigh, mHalfScollBarHeight - mHalfThumbHeight, mOffsetHigh
                + mThumbWidth, mHalfScollBarHeight + mHalfThumbHeight);
        mThumbHigh.draw(canvas);

        if (mBarChangeListener != null) {

            double progressLow = formatDouble(mOffsetLow * (mMax - mMin) / mDistance + mMin);
            double progressHigh = formatDouble(mOffsetHigh * (mMax - mMin) / mDistance + mMin);
//            TyreLog.d(TAG, "onDraw-->mOffsetLow: " + mOffsetLow + "  mOffsetHigh: " + mOffsetHigh
//                    + "  progressLow: " + progressLow + "  progressHigh: " + progressHigh);
            mBarChangeListener.onProgressChanged(this, progressLow, progressHigh, mMax, mMin);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            mFlag = getAreaFlag(e);
//            TyreLog.d(TAG, "e.getX: " + e.getX() + "mFlag: " + mFlag);
            if (mFlag == CLICK_ON_LOW) {
                mThumbLow.setState(STATE_PRESSED);
            } else if (mFlag == CLICK_ON_HIGH) {
                mThumbHigh.setState(STATE_PRESSED);
            } else if (mFlag == CLICK_IN_LOW_AREA) {
                mThumbLow.setState(STATE_PRESSED);
                if (e.getX() < 0 || e.getX() <= mThumbWidth) {
                    mOffsetLow = 0;
                } else if (e.getX() > mScollBarWidth - mThumbWidth) {
                    mOffsetLow = mDistance - mDuration;
                } else {
                    mOffsetLow = formatInt(e.getX() - (double)mThumbWidth / 2);
                    if (mOffsetHigh - mDuration <= mOffsetLow) {
                        mOffsetHigh = (mOffsetLow + mDuration <= mDistance) ? (mOffsetLow + mDuration)
                                : mDistance;
                        mOffsetLow = mOffsetHigh - mDuration;
                    }
                }
            } else if (mFlag == CLICK_IN_HIGH_AREA) {
                mThumbHigh.setState(STATE_PRESSED);
                if (e.getX() < mDuration) {
                    mOffsetHigh = mDuration;
                    mOffsetLow = mOffsetHigh - mDuration;
                } else if (e.getX() >= mScollBarWidth - mThumbWidth) {
                    mOffsetHigh = mDistance;
                } else {
                    mOffsetHigh = formatInt(e.getX() - (double)mThumbWidth / 2);
                    if (mOffsetHigh - mDuration <= mOffsetLow) {
                        mOffsetLow = (mOffsetHigh - mDuration >= 0) ? (mOffsetHigh - mDuration) : 0;
                        mOffsetHigh = mOffsetLow + mDuration;
                    }
                }
            }
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            if (mFlag == CLICK_ON_LOW) {
                if (e.getX() < 0 || e.getX() <= mThumbWidth) {
                    mOffsetLow = 0;
                } else if (e.getX() > mScollBarWidth - mThumbWidth - mDuration) {
                    mOffsetLow = mDistance - mDuration;
                    mOffsetHigh = mOffsetLow + mDuration;
                } else {
                    mOffsetLow = formatInt(e.getX() - (double)mThumbWidth / 2);
                    if (mOffsetHigh - mOffsetLow <= mDuration) {
                        mOffsetHigh = (mOffsetLow + mDuration <= mDistance) ? (mOffsetLow + mDuration)
                                : mDistance;
                    }
                }
            } else if (mFlag == CLICK_ON_HIGH) {
                if (e.getX() < mDuration + mThumbWidth) {
                    mOffsetHigh = mDuration;
                    mOffsetLow = 0;
                } else if (e.getX() > mScollBarWidth - mThumbWidth) {
                    mOffsetHigh = mDistance;
                } else {
                    mOffsetHigh = formatInt(e.getX() - (double)mThumbWidth / 2);
                    if (mOffsetHigh - mOffsetLow <= mDuration) {
                        mOffsetLow = (mOffsetHigh - mDuration >= 0) ? (mOffsetHigh - mDuration) : 0;
                    }
                }
            }
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            mThumbLow.setState(STATE_NORMAL);
            mThumbHigh.setState(STATE_NORMAL);
        }

        setProgressLow(formatDouble(mOffsetLow * (mMax - mMin) / mDistance + mMin));
        setProgressHigh(formatDouble(mOffsetHigh * (mMax - mMin) / mDistance + mMin));

        return true;
    }

    public int getAreaFlag(MotionEvent e) {
        int top = mHalfScollBarHeight - mHalfThumbHeight;
        int bottom = mHalfScollBarHeight + mHalfThumbHeight;

        if (e.getY() >= top && e.getY() <= bottom && e.getX() >= mOffsetLow
                && e.getX() <= mOffsetLow + mThumbWidth) {
            return CLICK_ON_LOW;
        } else if (e.getY() >= top && e.getY() <= bottom && e.getX() >= mOffsetHigh
                && e.getX() <= mOffsetHigh + mThumbWidth) {
            return CLICK_ON_HIGH;
        } else if (e.getY() >= top
                && e.getY() <= bottom
                && ((e.getX() >= 0 && e.getX() < mOffsetLow) || ((e.getX() > mOffsetLow
                + mThumbWidth) && e.getX() <= ((double)mOffsetHigh + mOffsetLow + mThumbWidth) / 2))) {
            return CLICK_IN_LOW_AREA;
        } else if (e.getY() >= top
                && e.getY() <= bottom
                && (((e.getX() > ((double)mOffsetHigh + mOffsetLow + mThumbWidth) / 2) && e.getX() < mOffsetHigh) || (e
                .getX() > mOffsetHigh + mThumbWidth && e.getX() <= mScollBarWidth))) {
            return CLICK_IN_HIGH_AREA;
        } else if (!(e.getX() >= 0 && e.getX() <= mScollBarWidth && e.getY() >= top && e.getY() <= bottom)) {
            return CLICK_OUT_AREA;
        } else {
            return CLICK_INVAILD;
        }
    }

    public void setMax(double max) {
        this.mMax = (float)max;
    }

    public double getMax() {
        return mMax;
    }

    public void setMin(double min) {
        this.mMin = (float)min;
    }

    public double getMin() {
        return mMin;
    }

    public void setProgressLow(double progressLow) {
        mOffsetLow = formatInt((progressLow - mMin) / (mMax - mMin) * mDistance);
        invalidate();
    }

    public void setProgressHigh(double progressHigh) {
        mOffsetHigh = formatInt((progressHigh - mMin) / (mMax - mMin) * mDistance);
        invalidate();
    }

    public double getProgressLow() {
        return formatDouble(mOffsetLow * (mMax - mMin) / mDistance + mMin);
    }

    public double getProgressHigh() {
        return formatDouble(mOffsetHigh * (mMax - mMin) / mDistance + mMin);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener mListener) {
        this.mBarChangeListener = mListener;
    }

    public interface OnSeekBarChangeListener {
        public void onProgressChanged(SeekBarPressure seekBar, double progressLow,
                                      double progressHigh, double max, double min);
    }

    /*
     * public void setDrawableState(Drawable dr) {
     * dr.setState(PRESSED_STATE_SET); invalidate(); }
     * @Override protected void drawableStateChanged() {
     * super.drawableStateChanged(); TyreLog.d(TAG, "drawableStateChanged");
     * mThumbLow.setState(PRESSED_STATE_SET);
     * mThumbHigh.setState(PRESSED_STATE_SET); invalidate(); }
     */

    private int formatInt(double value) {
        BigDecimal bd = new BigDecimal(value);
        BigDecimal bd1 = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        return bd1.intValue();
    }
}
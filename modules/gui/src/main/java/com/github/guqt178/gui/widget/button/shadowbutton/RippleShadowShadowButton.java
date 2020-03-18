package com.github.guqt178.gui.widget.button.shadowbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.guqt178.gui.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 可设置波纹阴影效果的Button
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:11
 */
public class RippleShadowShadowButton extends BaseShadowButton {

    private int mRoundRadius;
    private int mRippleColor;
    private int mRippleDuration;
    private int mRippleRadius;
    private float pointX, pointY;

    private Paint mRipplePaint;
    private RectF mRectF;
    private Path mPath;
    private Timer mTimer;
    private TimerTask mTask;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_DRAW_COMPLETE) {
                invalidate();
            }
        }
    };

    private int mRippleAlpha;
    // private final static int HALF_ALPHA = 127;
    private final static int RIPPLR_ALPHA = 47;
    private final static int MSG_DRAW_COMPLETE = 101;

    public RippleShadowShadowButton(Context context) {
        super(context);
    }

    public RippleShadowShadowButton(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RippleShadowShadowButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(final Context context, final AttributeSet attrs) {
        super.init(context, attrs);
        if (isInEditMode()) {
            return;
        }
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowButton);
        mRippleColor = typedArray.getColor(R.styleable.ShadowButton_sb_ripple_color, getResources().getColor(R.color.default_shadow_button_ripple_color));
        mRippleAlpha = typedArray.getInteger(R.styleable.ShadowButton_sb_ripple_alpha, RIPPLR_ALPHA);
        mRippleDuration = typedArray.getInteger(R.styleable.ShadowButton_sb_ripple_duration, 1000);
        mShapeType = typedArray.getInt(R.styleable.ShadowButton_sb_shape_type, 1);
        mRoundRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowButton_sb_radius, getResources().getDimensionPixelSize(R.dimen.default_shadow_button_radius));
        typedArray.recycle();
        mRipplePaint = new Paint();
        mRipplePaint.setColor(mRippleColor);
        mRipplePaint.setAlpha(mRippleAlpha);
        mRipplePaint.setStyle(Paint.Style.FILL);
        mRipplePaint.setAntiAlias(true);
        mPath = new Path();
        mRectF = new RectF();
        pointY = pointX = -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRipplePaint == null) {
            return;
        }
        drawFillCircle(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pointX = event.getX();
            pointY = event.getY();
            onStartDrawRipple();
        }
        return super.onTouchEvent(event);
    }

    /**
     * Draw ripple effect
     */
    private void drawFillCircle(Canvas canvas) {
        if (canvas != null && pointX >= 0 && pointY >= 0) {
            int rbX = canvas.getWidth();
            int rbY = canvas.getHeight();
            float x_max = Math.max(pointX, Math.abs(rbX - pointX));
            float y_max = Math.max(pointY, Math.abs(rbY - pointY));
            float longDis = (float) Math.sqrt(x_max * x_max + y_max * y_max);
            if (mRippleRadius > longDis) {
                onCompleteDrawRipple();
                return;
            }
            final float drawSpeed = longDis / mRippleDuration * 35;
            mRippleRadius += drawSpeed;

            canvas.save();
            // canvas.translate(0, 0);//保持原点
            mPath.reset();
            canvas.clipPath(mPath);
            if (mShapeType == 0) {
                mPath.addCircle(rbX >> 1, rbY >> 1, mWidth >> 1, Path.Direction.CCW);
            } else {
                mRectF.set(0, 0, mWidth, mHeight);
                mPath.addRoundRect(mRectF, mRoundRadius, mRoundRadius, Path.Direction.CCW);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipPath(mPath);
            } else {
                canvas.clipPath(mPath, Region.Op.REPLACE);
            }
            canvas.drawCircle(pointX, pointY, mRippleRadius, mRipplePaint);
            canvas.restore();
        }
    }

    /**
     * Start draw ripple effect
     */
    private void onStartDrawRipple() {
        onCompleteDrawRipple();
        mTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_DRAW_COMPLETE);
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask, 0, 30);
    }

    /**
     * Stop draw ripple effect
     */
    private void onCompleteDrawRipple() {
        mHandler.removeMessages(MSG_DRAW_COMPLETE);
        if (mTimer != null) {
            if (mTask != null) {
                mTask.cancel();
            }
            mTimer.cancel();
        }
        mRippleRadius = 0;
    }

    public int getRoundRadius() {
        return mRoundRadius;
    }

    public void setRoundRadius(int roundRadius) {
        mRoundRadius = roundRadius;
        invalidate();
    }

    public int getRippleColor() {
        return mRippleColor;
    }

    public void setRippleColor(int rippleColor) {
        mRippleColor = rippleColor;
    }

    public int getRippleDuration() {
        return mRippleDuration;
    }

    public void setRippleDuration(int rippleDuration) {
        mRippleDuration = rippleDuration;
    }

    public int getRippleRadius() {
        return mRippleRadius;
    }

    public void setRippleRadius(int rippleRadius) {
        mRippleRadius = rippleRadius;
    }

    public int getRippleAlpha() {
        return mRippleAlpha;
    }

    public void setRippleAlpha(int rippleAlpha) {
        mRippleAlpha = rippleAlpha;
    }
}

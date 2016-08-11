package net.qiujuer.sample.touchpull;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

/**
 * Created by JuQiu
 * on 16/8/11.
 */
public class PullView extends View {
    private static final float TOUCH_MOVE_MAX_Y = 160;

    private Path mPath = new Path();
    private Paint mPaint;

    private int mRadius;
    private int mCirclePointX, mCirclePointY;

    private float mProgress;
    private float mTouchMoveStartY;

    private int mWidth;
    private int mStartHeight, mEndHeight;

    public PullView(Context context) {
        super(context);
        init();
    }

    public PullView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PullView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0x80606060);
        paint.setStyle(Paint.Style.FILL);
        mPaint = paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = (int) (w * 0.8f);
        mStartHeight = (int) (mWidth * 1.2f);
        mEndHeight = (int) (mWidth * 1.8f);

        initPath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float tranX = (getWidth() - mWidth) / 2f;
        canvas.save();
        canvas.translate(tranX, 0);

        canvas.drawPath(mPath, mPaint);
        canvas.drawCircle(mCirclePointX, mCirclePointY, mRadius, mPaint);

        canvas.restore();
    }

    private void initPath() {
        final float progress = mProgress;
        final float w = mWidth;
        final float h = mStartHeight + (mEndHeight - mStartHeight) * mProgress;

        float cPointX = w / 2.0f;
        float cRadius = cPointX * 0.8f;
        float cPointY = h - cRadius;

        mCirclePointX = (int) cPointX;
        mCirclePointY = (int) cPointY;
        mRadius = (int) cRadius;

        Path path = mPath;
        path.reset();

        float leftToPointX = cPointX - cRadius;
        float leftToPointY = cPointY;

        float controlLineW = getControlLineWidth(cRadius * 0.9f, cRadius * 0.01f, progress);
        float leftControlPointX = cPointX - controlLineW;
        float leftControlPointY = cPointX / cPointY * leftControlPointX;

        path.quadTo(leftControlPointX, leftControlPointY, leftToPointX, leftToPointY);
        path.lineTo(cPointX + cRadius, cPointY);
        path.quadTo(cPointX + controlLineW, leftControlPointY, w, 0);

        invalidate();
    }

    private float getControlLineWidth(float startWidth, float endWidth, float progress) {
        return startWidth + (endWidth - startWidth) * progress;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchMoveStartY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                if (mTouchMoveStartY <= y) {
                    float moveSize = y - mTouchMoveStartY;
                    setProgress(moveSize > TOUCH_MOVE_MAX_Y ? 1 : moveSize / TOUCH_MOVE_MAX_Y);
                }
                return true;
            default:
                animToOrigin();
                return super.onTouchEvent(event);
        }
    }

    public void setProgress(float progress) {
        mProgress = progress;
        initPath();
    }

    private ValueAnimator mValueAnimator;

    private void animToOrigin() {
        if (mValueAnimator == null) {
            ValueAnimator animator = ValueAnimator.ofFloat(mProgress, 0f);
            animator.setDuration(260);
            animator.setInterpolator(new AnticipateOvershootInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Object obi = valueAnimator.getAnimatedValue();
                    if (obi instanceof Float)
                        setProgress((Float) obi);
                }
            });
            mValueAnimator = animator;
        }
        mValueAnimator.start();
    }
}
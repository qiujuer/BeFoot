package net.qiujuer.widget.touchpull;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by JuQiu
 * on 16/8/11.
 */
public class TouchPullView extends View {
    private final static int MIN_RUN_ANGLE = 90;
    private Path mPath = new Path();
    private Paint mPaint;

    // The value 0.0f ~ 1.0f
    private float mProgress;

    private Interpolator mRealProgressInterpolator = new DecelerateInterpolator();
    // The drag Interpolator to progress
    private Interpolator mTangentInterpolator = mRealProgressInterpolator;
    // The value mast between 0~45
    private int mTangentAngle = 30;
    // The value mast > mCircleRadius * 2
    private int mDragHeight = 50;

    // Circle parameters
    private int mCircleRadius = 25;
    private float mCirclePointX, mCirclePointY;

    private int mTargetWidth = -1;
    private int mTargetGravityHeight = -1;


    public TouchPullView(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public TouchPullView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public TouchPullView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TouchPullView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        // init paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0x20000000);
        paint.setAntiAlias(true);
        paint.setDither(true);
        mPaint = paint;

        if (attrs == null)
            return;

        final Context context = getContext();

        // Load attributes
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TouchPullView,
                defStyleAttr, defStyleRes);

        int color = array.getColor(R.styleable.TouchPullView_pColor, 0x20000000);

        int radius = array.getDimensionPixelOffset(R.styleable.TouchPullView_pRadius, mCircleRadius);
        int dragHeight = array.getDimensionPixelOffset(R.styleable.TouchPullView_pDragHeight, mDragHeight);
        int angle = array.getInteger(R.styleable.TouchPullView_pTangentAngle, mTangentAngle);

        int targetWidth = array.getDimensionPixelOffset(R.styleable.TouchPullView_pTargetWidth, mTargetWidth);
        int targetGravityHeight = array.getDimensionPixelOffset(R.styleable.TouchPullView_pTargetGravityHeight,
                mTargetGravityHeight);

        array.recycle();

        // Set values
        paint.setColor(color);

        setTangentAngle(angle);
        setRadius(radius);
        setDragHeight(dragHeight);

        setTargetWidth(targetWidth);
        setTargetGravityHeight(targetGravityHeight);
    }

    public int getTargetWidth() {
        return mTargetWidth;
    }

    public void setTargetWidth(int width) {
        if (width > 0)
            mTargetWidth = width;
    }

    public int getTargetGravityHeight() {
        return mTargetGravityHeight;
    }

    public void setTargetGravityHeight(int height) {
        if (height > 0)
            this.mTargetGravityHeight = height;
    }

    public void setTangentAngle(int angle) {
        if (angle >= 0 && angle <= 45) {
            mTangentAngle = angle;
            initTangentInterpolator();
        }
    }

    public void setDragHeight(int height) {
        if (height >= (mCircleRadius * 2)) {
            mDragHeight = height;
            initTangentInterpolator();
        }
    }

    public void setRadius(int radius) {
        if (radius > 0) {
            mCircleRadius = radius;
            int diameter = 2 * radius;
            if (mDragHeight < diameter) {
                mDragHeight = diameter;
            }
            initTangentInterpolator();
        }
    }

    private void initTangentInterpolator() {
        if (mCircleRadius > 0 && (mDragHeight >= mCircleRadius * 2)) {
            // The run // 270~360~mTangentAngle=>0~90~mTangentAngle
            // When drag double radius the angle = MIN_RUN_ANGLE
            mTangentInterpolator = PathInterpolatorCompat.create((mCircleRadius * 2) / mDragHeight,
                    MIN_RUN_ANGLE / (MIN_RUN_ANGLE + mTangentAngle));
        }
    }

    public void setProgress(float progress) {
        if (progress < 0 || progress > 1)
            return;
        Animator animator = mValueAnimator;
        if (animator != null) {
            animator.cancel();
        }
        doProgressChange(progress);
    }

    private void doProgressChange(float progress) {
        mProgress = progress;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int iHeight = (int) ((mDragHeight * mProgress + 0.5f) + getPaddingTop() + getPaddingBottom());
        int iWidth = Math.max(2 * mCircleRadius, mTargetWidth) + getPaddingLeft() + getPaddingRight();

        int measuredWidth;
        int measuredHeight;

        if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = Math.min(widthSize, iWidth);
        } else {
            measuredWidth = iWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            measuredHeight = Math.min(heightSize, iHeight);
        } else {
            measuredHeight = iHeight;
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mTargetWidth == -1) {
            mTargetWidth = w;
        }

        if (mTargetGravityHeight == -1) {
            mTargetGravityHeight = (int) (mCircleRadius * 0.25f);
        }

        updatePath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float tranX = (getWidth() - getValueByLine(getWidth(), mTargetWidth, mProgress)) / 2f;
        canvas.save();
        canvas.translate(tranX, 0);

        canvas.drawPath(mPath, mPaint);
        canvas.drawCircle(mCirclePointX, mCirclePointY, mCircleRadius, mPaint);

        canvas.restore();
    }

    private void updatePath() {
        final float progress = mRealProgressInterpolator.getInterpolation(mProgress);
        final float w = getValueByLine(getWidth(), mTargetWidth, mProgress);
        final float h = getValueByLine(0, mDragHeight, mProgress);

        final float cPointX = w / 2.0f;
        final float cRadius = mCircleRadius;
        final float cPointY = h - cRadius;
        final float endControlY = mTargetGravityHeight;

        mCirclePointX = cPointX;
        mCirclePointY = cPointY;

        Path path = mPath;
        path.reset();
        path.moveTo(0, 0);

        float leftToPointX, leftToPointY;
        float leftControlPointX, leftControlPointY;

        double radian = Math.toRadians((MIN_RUN_ANGLE + mTangentAngle) * mTangentInterpolator.getInterpolation(progress));
        float x = (float) (Math.sin(radian) * cRadius);
        float y = (float) (Math.cos(radian) * cRadius);

        leftToPointX = cPointX - x;
        leftToPointY = cPointY + y;

        leftControlPointY = getValueByLine(0, endControlY, progress);
        float th = leftToPointY - leftControlPointY;
        float tw = (float) (th / Math.tan(radian));
        leftControlPointX = leftToPointX - tw;

        path.quadTo(leftControlPointX, leftControlPointY, leftToPointX, leftToPointY);
        path.lineTo(cPointX + cPointX - leftToPointX, leftToPointY);
        path.quadTo(cPointX + cPointX - leftControlPointX, leftControlPointY, w, 0);

        invalidate();
    }

    private float getValueByLine(float start, float end, float progress) {
        return start + (end - start) * progress;
    }


    private ValueAnimator mValueAnimator;

    public void animToOrigin() {
        if (mValueAnimator == null) {
            ValueAnimator animator = ValueAnimator.ofFloat(mProgress, 0f);
            animator.setDuration(400);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Object obi = valueAnimator.getAnimatedValue();
                    if (obi instanceof Float)
                        doProgressChange((Float) obi);
                }
            });
            mValueAnimator = animator;
        } else {
            mValueAnimator.cancel();
            mValueAnimator.setFloatValues(mProgress, 0f);
        }
        mValueAnimator.start();
    }
}
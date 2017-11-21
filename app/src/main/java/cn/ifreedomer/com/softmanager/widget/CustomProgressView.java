package cn.ifreedomer.com.softmanager.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import java.lang.reflect.Method;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.util.DrawableUtils;

/**
 * 首页自定义进度view
 *
 * @author 伍仪华
 */
public class CustomProgressView extends View {
    private Bitmap percent_bg_shadow;
    private Bitmap percent_bg;
    private int width;
    private int height;
    private Paint bgPaint;
    private Paint paint;
    private Paint circlePaint;
    private CustomAnimation mCustomAnimation;
    private float sweepAngle = 90;
    private int startAngle = -90;
    private int MaxAngle = 270;
    private boolean big;
    private int progress;
    private TextView percent_textview;

    public CustomProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressView(Context context) {
        super(context);
    }

    @SuppressWarnings("rawtypes")
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                boolean bool = ((Boolean) View.class.getMethod("isHardwareAccelerated", new Class[0]).invoke(this, new Object[0])).booleanValue();
                if (bool) {
                    Class[] arrayOfClass = new Class[2];
                    arrayOfClass[0] = Integer.TYPE;
                    arrayOfClass[1] = Paint.class;
                    Method localMethod = View.class.getMethod("setLayerType", arrayOfClass);
                    Object[] arrayOfObject = new Object[2];
                    arrayOfObject[0] = Integer.valueOf(1);
                    arrayOfObject[1] = null;
                    localMethod.invoke(this, arrayOfObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (this.progress < 3) {
            this.progress = 3;
        }
        int duration = getDuration(this.progress);
        sweepAngle = (this.progress * (MaxAngle - startAngle) / 100 + startAngle);

        mCustomAnimation = new CustomAnimation(this, startAngle, sweepAngle, duration);
        mCustomAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        super.startAnimation(mCustomAnimation);
        mCustomAnimation.setAnimationListener(new CustomAnimationListener(this));
    }

    public void setTitle(String title, boolean big) {
        this.big = big;
    }

    public void setBig(boolean big) {
        this.big = big;
    }

    public void setPercentView(TextView percent_textview) {
        this.percent_textview = percent_textview;
        this.percent_textview.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/RedMoonRising.ttf"));
        //this.percent_textview.setShadowLayer(2, 2, 5, 0x70000000);
    }

    public int getDuration(int paramInt) {
        int i1 = paramInt * 45;
        if (i1 > 1800) {
            i1 = 1800;
        }
        return i1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (percent_bg_shadow == null) {
            width = getWidth();
            height = width;
            Bitmap bitmap = null;
            if (big) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.big_circle_line);
                percent_bg_shadow = DrawableUtils.scaleTo(bitmap, width, height);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.small_circle_line);
                percent_bg_shadow = DrawableUtils.scaleTo(bitmap, width - 20, height - 20);
            }
        }
        if (percent_bg == null) {
            Bitmap bitmap = null;
            if (big) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.big_circle);
                percent_bg = DrawableUtils.scaleTo(bitmap, width, height);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.small_circle);
                percent_bg = DrawableUtils.scaleTo(bitmap, width - 20, height - 20);
            }
        }

        if (bgPaint == null) {
            bgPaint = new Paint();
            bgPaint.setAntiAlias(true);
            bgPaint.setDither(false);
        }

        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(false);
        }

        if (circlePaint == null) {
            circlePaint = new Paint();
            circlePaint.setAntiAlias(true);
            circlePaint.setDither(false);
            circlePaint.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        }

        int progress = getNumberProgress();
        canvas.save();
        if (big) {
            canvas.drawBitmap(percent_bg_shadow, 0, 0, bgPaint);
            getSectorClip(canvas, width / 2, height / 2, width / 2, -90, getAnimProgress());
            canvas.drawBitmap(percent_bg, 0, 0, paint);
            canvas.restore();
        } else {
            canvas.drawArc(new RectF(0, 0, width, height), 0, 360, true, circlePaint);
            canvas.drawBitmap(percent_bg_shadow, 10, 10, bgPaint);
            getSectorClip(canvas, width / 2, height / 2, (width - 20) / 2, -90, getAnimProgress());
            canvas.drawBitmap(percent_bg, 10, 10, paint);
            canvas.restore();
        }
        if (percent_textview != null) {
            percent_textview.setText(progress + "");
        }
    }

    private void getSectorClip(Canvas canvas, float center_X, float center_Y, float r, float startAngle, float endAngle) {
        Path path = new Path();
        // 下面是获得一个三角形的剪裁区
        path.moveTo(center_X, center_Y); // 圆心
        path.lineTo((float) (center_X + r * Math.cos(startAngle * Math.PI / 180)), // 起始点角度在圆上对应的横坐标
                (float) (center_Y + r * Math.sin(startAngle * Math.PI / 180))); // 起始点角度在圆上对应的纵坐标
        path.lineTo((float) (center_X + r * Math.cos(endAngle * Math.PI / 180)), // 终点角度在圆上对应的横坐标
                (float) (center_Y + r * Math.sin(endAngle * Math.PI / 180))); // 终点角度在圆上对应的纵坐标
        path.close();


//		// 剪切出进度条头部半圆
//		if(big){
//			float f6 = center_X + 14.5F * (float) (r * Math.cos(endAngle * Math.PI / 180)) / 15.0F;
//			float f7 = center_Y + 14.5F * (float) (r * Math.sin(endAngle * Math.PI / 180)) / 15.0F;
//			path.addArc(new RectF(f6 - r / 30.0F, f7 - r / 30.0F, f6 + r / 30.0F, f7 + r / 30.0F), endAngle, 180.0F);
//		}else{
//			float f6 = center_X + 14.1F * (float) (r * Math.cos(endAngle * Math.PI / 180)) / 15.0F;
//			float f7 = center_Y + 14.1F * (float) (r * Math.sin(endAngle * Math.PI / 180)) / 15.0F;
//			path.addArc(new RectF(f6 - r / 17F, f7 - r / 17F, f6 + r / 17F, f7 + r / 17F), endAngle, 180.0F);
//		}

        // 下面是获得弧形剪裁区的方法
        path.addArc(new RectF(center_X - r, center_Y - r, center_X + r, center_Y + r), startAngle, endAngle - startAngle);

        //canvas.drawPath(path, paint);
        canvas.clipPath(path);
    }

    private float getAnimProgress() {
        float f1;
        if (getAnimation() != null) {
            f1 = mCustomAnimation.getAnimProgress();
        } else {
            f1 = sweepAngle;
        }
        return f1;
    }

    private int getNumberProgress() {
        int pro;
        if (getAnimation() != null) {
            pro = mCustomAnimation.getNumberProgress();
        } else {
            pro = progress;
        }
        return pro;
    }

    class CustomAnimation extends Animation {
        private CustomProgressView customProgressView;
        private float animStartAngle = 0.0F;
        private float animSweepAngle = 0.0F;
        private float currentAngle = 0.0F;
        private int currentProgress;

        public CustomAnimation(CustomProgressView paramPercentSurfaceView, float paramFloat1, float paramFloat2, long paramLong) {
            this.customProgressView = paramPercentSurfaceView;
            this.currentAngle = paramFloat1;
            this.animStartAngle = paramFloat1;
            this.animSweepAngle = paramFloat2;
            setDuration(paramLong);
        }

        public float getAnimProgress() {
            return this.currentAngle;
        }

        public int getNumberProgress() {
            return this.currentProgress;
        }

        protected void applyTransformation(float paramFloat, Transformation paramTransformation) {
            this.currentAngle = ((float) ((float) (1.0D / Math.sin(1.919862151145935D)) * Math.sin(1.919862F * paramFloat))
                    * (this.animSweepAngle - this.animStartAngle) + this.animStartAngle);

            this.currentProgress = (int) (progress * paramFloat);
            customProgressView.invalidate();
        }

        public void initialize(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
            super.initialize(paramInt1, paramInt2, paramInt3, paramInt4);
        }

        public void setAnimationListener(Animation.AnimationListener paramAnimationListener) {
            super.setAnimationListener(paramAnimationListener);
        }
    }

    class CustomAnimationListener implements Animation.AnimationListener {
        private CustomProgressView customProgressView;

        public CustomAnimationListener(CustomProgressView paramPercentSurfaceView) {
            this.customProgressView = paramPercentSurfaceView;
        }

        public void onAnimationEnd(Animation paramAnimation) {
            customProgressView.setDrawingCacheEnabled(true);
        }

        public void onAnimationRepeat(Animation paramAnimation) {
        }

        public void onAnimationStart(Animation paramAnimation) {
            customProgressView.setDrawingCacheEnabled(false);
        }
    }

}

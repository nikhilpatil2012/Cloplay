/*
package tronbox.cloplay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class FingerPaintActivity extends Activity
{
    MyView mv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mv= new MyView(this);
        setContentView(mv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setARGB(255, 0, 0, 0);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{ 11, 15, }, 0));
        mPaint.setStrokeWidth(6);

    }

    private Paint mPaint;

    public class MyView extends View {
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint   mBitmapPaint;
        Context context;

        public MyView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            mBitmapPaint.setColor(Color.parseColor("#e3b333"));
        }
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mPath.moveTo(0, h / 2);
            mPath.lineTo(w, h / 2);
            mPath.lineTo(w, (h/2)+100);
            mPath.lineTo(0, (h/2)+100);



        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawPath(mPath, mPaint);
            //canvas.drawLine(10,10, 200,10, mPaint);
        }
    }
}

*/

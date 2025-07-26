package com.example.enhancingyourviews;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawingThread drawingThread;
    private Paint paint;

    public CustomSurfaceView(Context context) {
        super(context);
        init();
    }

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        getHolder().addCallback(this);

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        if (isHardwareAccelerated()) {
            paint.setAntiAlias(true);
        } else {
            paint.setAntiAlias(false);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawingThread = new DrawingThread(getHolder());
        drawingThread.setRunning(true);
        drawingThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawingThread.setRunning(false);
        while (retry) {
            try {
                drawingThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class DrawingThread extends Thread {

        private SurfaceHolder surfaceHolder;
        private boolean running;

        public DrawingThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            while (running) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        synchronized (surfaceHolder) {
                            drawCanvas(canvas);
                        }
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        private void drawCanvas(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 100, paint);
        }
    }
}

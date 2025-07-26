package com.example.invadinghomescreen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class MyLiveWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends Engine {
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLUE);
            holder.unlockCanvasAndPost(canvas);
        }
    }
}

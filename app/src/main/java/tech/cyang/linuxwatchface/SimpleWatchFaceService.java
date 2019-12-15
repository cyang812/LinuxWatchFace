package tech.cyang.linuxwatchface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.util.Log;
import android.view.SurfaceHolder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.TimeUnit;

public class SimpleWatchFaceService extends CanvasWatchFaceService {

    private static final long TICK_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(1);

    @Override
    public Engine onCreateEngine() {
        return new SimpleEngine();
    }

    private class SimpleEngine extends CanvasWatchFaceService.Engine {

        private static final String ACTION_TIME_ZONE = "time-zone";
        private static final String TAG = "SimpleEngine";

        private SimpleWatchFace watchFace;
        private Handler timeTick;
        private GoogleApiClient googleApiClient;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(SimpleWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setAmbientPeekMode(WatchFaceStyle.AMBIENT_PEEK_MODE_HIDDEN)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());

            timeTick = new Handler(Looper.myLooper());
            startTimerIfNecessary();

            watchFace = SimpleWatchFace.newInstance(SimpleWatchFaceService.this);
        }

        private void startTimerIfNecessary() {
            timeTick.removeCallbacks(timeRunnable);
            if (isVisible() && !isInAmbientMode()) {
                timeTick.post(timeRunnable);
            }
        }

        private final Runnable timeRunnable = new Runnable() {
            @Override
            public void run() {
                onSecondTick();

                if (isVisible() && !isInAmbientMode()) {
                    timeTick.postDelayed(this, TICK_PERIOD_MILLIS);
                }
            }
        };

        private void onSecondTick() {
            invalidateIfNecessary();
        }

        private void invalidateIfNecessary() {
            if (isVisible() && !isInAmbientMode()) {
                invalidate();
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                registerTimeZoneReceiver();
            } else {
                unregisterTimeZoneReceiver();
            }
            startTimerIfNecessary();
        }

        private void unregisterTimeZoneReceiver() {
            unregisterReceiver(timeZoneChangedReceiver);
        }

        private void registerTimeZoneReceiver() {
            IntentFilter timeZoneFilter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            registerReceiver(timeZoneChangedReceiver, timeZoneFilter);
        }

        private BroadcastReceiver timeZoneChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
                    watchFace.updateTimeZoneWith(intent.getStringExtra(ACTION_TIME_ZONE));
                }
            }
        };

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            super.onDraw(canvas, bounds);
            watchFace.draw(canvas, bounds);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            watchFace.setShowSeconds(!isInAmbientMode());

            if (inAmbientMode) {
                watchFace.updateBackgroundColourToDefault();
                watchFace.updateAllItemsColourToDefault();
            } else {
                watchFace.restoreBackgroundColour();
                watchFace.restoreAllItemsColour();
            }

            invalidate();
            startTimerIfNecessary();
        }

        @Override
        public void onDestroy() {
            timeTick.removeCallbacks(timeRunnable);
            super.onDestroy();
        }
    }
}

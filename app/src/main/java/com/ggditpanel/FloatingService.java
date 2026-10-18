package com.ggditpanel;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatingService extends Service {

    static {
        System.loadLibrary("native-lib");
    }

    public native String[] getFeatureList();
    public static native void Preferences_Changes(int featNum, String featName, int value, boolean booleanValue, String str);

    private WindowManager windowManager;
    private View container;
    private WindowManager.LayoutParams params;

    private float dX, dY, startX, startY;

    private LinearLayout menuLayout, contentLayout, containsLayout;

    private Handler scanHandler;
    private Runnable scanRunnable;

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        container = LayoutInflater.from(this).inflate(R.layout.floating_layout, null);

        menuLayout = container.findViewById(R.id.MENU);
        contentLayout = container.findViewById(R.id.LIN_SCROLL);
        containsLayout = container.findViewById(R.id.CONTAINS);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        windowManager.addView(container, params);

        container.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = params.x - event.getRawX();
                    dY = params.y - event.getRawY();
                    startX = event.getRawX();
                    startY = event.getRawY();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    params.x = (int) (event.getRawX() + dX);
                    params.y = (int) (event.getRawY() + dY);
                    windowManager.updateViewLayout(container, params);
                    return true;

                case MotionEvent.ACTION_UP:
                    float deltaX = Math.abs(event.getRawX() - startX);
                    float deltaY = Math.abs(event.getRawY() - startY);

                    if (deltaX < 10 && deltaY < 10) {
                        if (containsLayout.getVisibility() == View.VISIBLE) {
                            containsLayout.setVisibility(View.GONE);
                        } else {
                            containsLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    return true;
            }
            return false;
        });

        String[] features = getFeatureList();
        for (String feature : features) {
            addToggle(feature);
        }
    }

    private void handleFeature(int featNum, boolean enabled) {
        switch (featNum) {
            case 1:
                if (enabled) {

                } else {

                }
                break;
        }
    }

    private void addToggle(String raw) {
        String[] parts = raw.split("_", 3);
        int featNum = Integer.parseInt(parts[0]);
        String name = parts[2];

        int textColorDefault = getResources().getColor(R.color.OBS_COLOR);
        int bgColorDefault = getResources().getColor(R.color.TEXT_COLOR);

        TextView btn = new TextView(this);
        btn.setText(name + ": OFF");
        btn.setTextColor(textColorDefault);
        btn.setBackgroundColor(bgColorDefault);
        btn.setTextSize(14f);
        btn.setTypeface(null, android.graphics.Typeface.BOLD);
        btn.setPadding(10, 10, 10, 10);
        btn.setGravity(android.view.Gravity.CENTER);

        final boolean[] state = {false};

        btn.setOnClickListener(v -> {
            state[0] = !state[0];

            handleFeature(featNum, state[0]);

            Preferences_Changes(featNum, name, 0, state[0], null);

            btn.setText(name + (state[0] ? ": ON" : ": OFF"));

            if (state[0]) {
                btn.setTextColor(bgColorDefault);
                btn.setBackgroundColor(textColorDefault);
            } else {
                btn.setTextColor(textColorDefault);
                btn.setBackgroundColor(bgColorDefault);
            }
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        lp.setMargins(5, 5, 5, 5);
        contentLayout.addView(btn, lp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (container != null && container.isAttachedToWindow()) {
            windowManager.removeView(container);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
package vn.thutruong.bumxauto;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FloatingService extends Service {
    private WindowManager wm;
    private LinearLayout panel;
    private WindowManager.LayoutParams params;
    private boolean running = false;

    @Override public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(8, 8, 8, 8);
        panel.setBackgroundColor(0xEE14212B);

        Button toggle = new Button(this);
        toggle.setText("BUMX AUTO · START");
        toggle.setAllCaps(false);
        toggle.setOnClickListener(v -> {
            running = !running;
            toggle.setText(running ? "BUMX AUTO · PAUSE" : "BUMX AUTO · START");
            if (running) {
                Intent launch = getPackageManager().getLaunchIntentForPackage("com.mobile.bumx");
                if (launch != null) { launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(launch); }
                else Toast.makeText(this, "Chưa tìm thấy BUMX", Toast.LENGTH_SHORT).show();
            }
        });
        panel.addView(toggle);

        Button back = new Button(this);
        back.setText("Mở BUMX");
        back.setAllCaps(false);
        back.setOnClickListener(v -> {
            Intent launch = getPackageManager().getLaunchIntentForPackage("com.mobile.bumx");
            if (launch != null) { launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(launch); }
        });
        panel.addView(back);

        Button close = new Button(this);
        close.setText("Đóng menu");
        close.setAllCaps(false);
        close.setOnClickListener(v -> stopSelf());
        panel.addView(close);

        int type = Build.VERSION.SDK_INT >= 26 ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            : WindowManager.LayoutParams.TYPE_PHONE;
        params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.END;
        params.x = 20; params.y = 180;
        panel.setOnTouchListener(new View.OnTouchListener() {
            private int sx, sy; private float tx, ty;
            @Override public boolean onTouch(android.view.View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    sx = params.x; sy = params.y; tx = e.getRawX(); ty = e.getRawY(); return false;
                }
                if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    params.x = sx - (int)(e.getRawX() - tx);
                    params.y = sy + (int)(e.getRawY() - ty);
                    wm.updateViewLayout(panel, params); return true;
                }
                return false;
            }
        });
        wm.addView(panel, params);
    }

    @Override public void onDestroy() {
        if (panel != null && panel.isAttachedToWindow()) wm.removeView(panel);
        super.onDestroy();
    }
    @Override public IBinder onBind(Intent intent) { return null; }
}

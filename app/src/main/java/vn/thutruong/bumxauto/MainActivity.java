package vn.thutruong.bumxauto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private TextView status;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(28, 36, 28, 28);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setBackgroundColor(0xFF101820);

        TextView title = new TextView(this);
        title.setText("BUMX AUTO");
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(28);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Trợ lý nhận diện màn hình BUMX / TikTok");
        subtitle.setTextColor(0xFFB9C7D5);
        subtitle.setTextSize(15);
        subtitle.setPadding(0, 12, 0, 24);
        layout.addView(subtitle);

        status = new TextView(this);
        status.setText("Trạng thái: Chưa bật cửa sổ nổi");
        status.setTextColor(0xFFFFFFFF);
        status.setTextSize(14);
        status.setPadding(0, 12, 0, 18);
        layout.addView(status);

        addButton(layout, "1. Cấp quyền cửa sổ nổi", v -> {
            if (!Settings.canDrawOverlays(this)) {
                Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
                startActivity(i);
            } else toast("Đã có quyền cửa sổ nổi");
        });

        addButton(layout, "2. Bật nhận diện màn hình", v ->
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));

        addButton(layout, "3. Bật menu nổi", v -> {
            if (!Settings.canDrawOverlays(this)) {
                toast("Hãy cấp quyền cửa sổ nổi trước");
                return;
            }
            startService(new Intent(this, FloatingService.class));
            status.setText("Trạng thái: Menu nổi đang chạy");
        });

        addButton(layout, "Mở BUMX", v -> openPackage("com.mobile.bumx"));
        addButton(layout, "Mở TikTok", v -> {
            if (!openPackage("com.zhiliaoapp.musically")) openPackage("com.ss.android.ugc.trill");
        });

        TextView note = new TextView(this);
        note.setText("Lưu ý: ứng dụng chỉ hỗ trợ mở ứng dụng, hiển thị menu nổi và nhận diện chữ trên màn hình. Các thao tác Like/Follow/Comment và xác nhận nhiệm vụ do bạn tự thực hiện.");
        note.setTextColor(0xFFFFD18A);
        note.setTextSize(13);
        note.setPadding(0, 22, 0, 0);
        layout.addView(note);

        setContentView(layout);
    }

    private void addButton(LinearLayout parent, String text, View.OnClickListener listener) {
        Button b = new Button(this);
        b.setText(text);
        b.setAllCaps(false);
        b.setOnClickListener(listener);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 8, 0, 8);
        parent.addView(b, lp);
    }

    private boolean openPackage(String packageName) {
        Intent launch = getPackageManager().getLaunchIntentForPackage(packageName);
        if (launch == null) { toast("Không tìm thấy ứng dụng: " + packageName); return false; }
        startActivity(launch);
        return true;
    }

    private void toast(String message) { Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); }
}

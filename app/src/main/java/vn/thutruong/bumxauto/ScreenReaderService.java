package vn.thutruong.bumxauto;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
import java.util.Locale;

public class ScreenReaderService extends AccessibilityService {
    private long lastNotice = 0;
    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null || event.getPackageName() == null) return;
        String pkg = event.getPackageName().toString();
        if (!pkg.equals("com.mobile.bumx") &&
            !pkg.equals("com.zhiliaoapp.musically") &&
            !pkg.equals("com.ss.android.ugc.trill")) return;

        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) return;
        StringBuilder visibleText = new StringBuilder();
        collectText(root, visibleText, 0);
        String text = visibleText.toString().toLowerCase(Locale.ROOT);
        String kind = "Chưa xác định loại màn hình";
        if (text.contains("comment") || text.contains("bình luận")) kind = "Có dấu hiệu nhiệm vụ bình luận";
        else if (text.contains("follow") || text.contains("theo dõi")) kind = "Có dấu hiệu nhiệm vụ theo dõi";
        else if (text.contains("like") || text.contains("thích") || text.contains("tim")) kind = "Có dấu hiệu nhiệm vụ thích";
        else if (text.contains("mở bài viết") || text.contains("open post")) kind = "Có nút mở bài viết";

        long now = System.currentTimeMillis();
        if (now - lastNotice > 5000 && !visibleText.toString().isEmpty()) {
            lastNotice = now;
            android.util.Log.i("BUMX_AUTO", pkg + " | " + kind);
        }
    }

    private void collectText(AccessibilityNodeInfo node, StringBuilder out, int depth) {
        if (node == null || depth > 30) return;
        CharSequence t = node.getText();
        CharSequence d = node.getContentDescription();
        if (t != null && t.length() > 0) out.append(t).append(' ');
        if (d != null && d.length() > 0) out.append(d).append(' ');
        for (int i = 0; i < node.getChildCount(); i++) collectText(node.getChild(i), out, depth + 1);
    }

    @Override public void onInterrupt() { }
}

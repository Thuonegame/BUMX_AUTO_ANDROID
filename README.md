# BUMX AUTO Android — bản khung V1

## Có trong bản này
- Màn hình chính Android.
- Yêu cầu quyền cửa sổ nổi.
- Menu nổi START/PAUSE, kéo thả được.
- Nút mở BUMX và TikTok.
- Accessibility Service có sự đồng ý của người dùng, chỉ đọc chữ hiển thị từ BUMX/TikTok và phân loại sơ bộ bằng từ khóa; kết quả ghi Logcat.
- GitHub Actions để biên dịch APK debug.

## Chưa có / chưa thể xác nhận
- Chưa có AI Vision thực sự; hiện tại mới là nhận diện chữ bằng Accessibility/OCR-like text extraction và luật từ khóa.
- Chưa kết nối API lấy danh sách nhiệm vụ của BUMX. Tài liệu SMM V2 không chứng minh có API nhiệm vụ.
- Chưa kiểm thử trên thiết bị thực tế.
- Không tự Like/Follow/Comment, không tự xác nhận hoàn thành nhiệm vụ.

## Build APK bằng GitHub
1. Tạo repository GitHub mới.
2. Giải nén ZIP và tải toàn bộ nội dung thư mục này lên repository (giữ nguyên cấu trúc).
3. Mở tab Actions, chọn `Build BUMX AUTO APK`, bấm `Run workflow`.
4. Khi workflow chạy thành công, mở lần chạy đó, tải Artifact `BUMX-AUTO-debug-APK`.
5. Giải nén Artifact để lấy `app-debug.apk`.

## Yêu cầu
- Android Studio/Gradle plugin 8.6.1, JDK 17, Android SDK platform 35.
- Package BUMX được cấu hình là `com.mobile.bumx`; TikTok package phổ biến là `com.zhiliaoapp.musically`. Một số bản TikTok có package khác.

## Quyền riêng tư
Accessibility chỉ bật khi người dùng tự vào Cài đặt và cho phép. Dịch vụ lọc theo package BUMX/TikTok, không gửi nội dung màn hình lên máy chủ. Mã nguồn hiện ghi nhãn nhận diện trong Logcat.

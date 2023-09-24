# BTL-PTHTTM-IrisRecognitionSystem

## Hệ thống kiểm tra an ninh bằng mống mắt
### Giới thiệu đề tài
- Nhờ sự phát triển vượt bậc của công nghệ, đặc biệt trong các lĩnh vực điện tử 
và khoa học máy tính, ngày nay các phương thức kiểm tra an ninh bằng các 
đặc điểm sinh học như khuôn mặt, dấu vân tay, đồng tử mắt, giọng nói,... đã 
thay thế các phương thức truyền thống bằng con người, chìa khóa, mật khẩu để
trở thành phương pháp kiểm tra an ninh được sử dung rộng rãi và đáng tin cậy.
- Trong các đặc điểm sinh học được dùng để kiểm tra an ninh, đồng tử mắt được 
coi là đặc điểm cho ta khả năng kiểm tra với độ chính xác và an toàn tốt nhất 
nhờ các đặc tính sau:
  - Độc nhất: Hai đồng tử mắt giống nhau là cực kì hiếm gặp, ngay cả trên các 
cặp song sinh hay một cá thể.
  - Ổn định: Đồng tử mắt được bảo vệ tốt, không thay đổi nhiều theo thời gian 
hay các điều kiện của môi trường.
  - Giàu thông tin: Cấu tạo của đồng tử mắt cho ta thu nhận được nhiều thông 
tin hơn các đặc điểm sinh học khác.
  - Không tiếp xúc: Kiểm tra đồng tử mắt không yêu cầu tiếp xúc vật lý với 
cảm biến.

### Các bài toán con cần xử lý
#### Xác định vùng ảnh chứa mống mắt
- Thuộc dạng bài toán Image Segmentation
- Mô hình: YOLOv8n-seg.
- Bộ dữ liệu huấn luyện: CASIA-IrisV2, vùng ảnh chứa mống mắt và đồng tử mắt được gán nhãn thủ công.
- Đầu vào: Hình ảnh chụp mắt người bằng máy ảnh chuyên dụng.
- Đầu ra: Vùng ảnh chứa mống mắt, đã tiền xử lý và chuẩn hóa theo phương pháp của Daugman.
#### Xác định người sở hữu mống mắt
- Thuộc dạng bài toán Image Classification
- Mô hình: Resnet34
- Bộ dữ liệu huấn luyện: vùng ảnh chứa mống mắt đã tiền xử lý và chuẩn hóa theo phương pháp của Daugman, gán nhãn theo người.
- Đầu vào: Đầu ra thuật toán 1.
- Đầu ra: Nhãn (người nào?)

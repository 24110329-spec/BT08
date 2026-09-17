# Dự án BT_API - Spring Boot 3 RESTful API & AJAX CRUD

## 📌 Giới thiệu
Dự án cung cấp các RESTful API và giao diện hiển thị qua AJAX (jQuery + Bootstrap 5) hỗ trợ đầy đủ chức năng:
- **CRUD** (Thêm, Xem, Sửa, Xóa) trên 2 bảng `Category` và `Product`.
- **Upload và quản lý file ảnh/icon**.
- **Tìm kiếm theo tên**.
- **Phân trang (Pagination)**.

## 🛠️ Công nghệ sử dụng
- Java 17+
- Spring Boot 3.1.5 (Spring Web, Spring Data JPA, Thymeleaf, Validation)
- MySQL / H2 Database
- Springdoc OpenAPI 3 (Swagger UI)
- jQuery, Bootstrap 5, FontAwesome

## 🚀 Khởi chạy dự án
1. Cấu hình kết nối Database trong file `src/main/resources/application.properties`.
2. Chạy ứng dụng bằng lệnh:
   ```bash
   mvn spring-boot:run
   ```
3. Truy cập trình duyệt:
   - **Quản lý Sản phẩm:** `http://localhost:8080/products`
   - **Quản lý Danh mục:** `http://localhost:8080/categories`
   - **Swagger 3 UI:** `http://localhost:8080/swagger-ui.html`
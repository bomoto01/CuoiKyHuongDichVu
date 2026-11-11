# Hệ Thống Quản Lý Thư Viện HaUI - Trường ĐH Công Nghiệp Hà Nội

Dự án thuộc học phần **Phát triển phần mềm hướng dịch vụ (SOA)**.

## 🚀 Công Nghệ Sử Dụng
- **Backend:** Java Spring Boot 3.3.5 (JDK 21/25), Spring Data JPA, Spring Security, JWT (jjwt 0.12.6), MapStruct, Lombok, Swagger/OpenAPI.
- **Database:** H2 In-Memory (mặc định sẵn sàng chạy ngay) & Microsoft SQL Server / MySQL.
- **Frontend:** React, Vite, Tailwind CSS, Lucide Icons, Axios.

## 📂 Cấu Trúc Dự Án
```
├── backend/
│   ├── src/main/java/com/haui/library/
│   │   ├── config/             # Cấu hình Security, CORS, Seed Data khởi tạo
│   │   ├── controller/         # REST Controllers theo đúng đặc tả Word
│   │   ├── dto/                # Request & Response DTOs
│   │   ├── entity/             # JPA Entities: Sach, TheLoai, KhachHang, TaiKhoan, PhieuMuon, LichSuGiaoDich
│   │   ├── enums/              # Role (ADMIN, MANAGER), TransactionStatus
│   │   ├── exception/          # GlobalExceptionHandler, AppException, ErrorCode
│   │   ├── mapper/             # MapStruct mappers
│   │   ├── repository/         # Spring Data JPA repositories
│   │   ├── security/           # JWT Provider, CustomUserDetailsService, JwtAuthFilter
│   │   └── service/            # Service interfaces & Implementations
│   └── pom.xml
└── frontend/                   # Ứng dụng React Frontend
```

## 🔑 Tài Khoản Mặc Định (Đã Seed Sẵn)
- **Quản trị viên (ADMIN):**
  - Username: `admin`
  - Password: `123456`
  - Toàn quyền quản lý Sách, Thể loại, Tài khoản nhân sự, Lịch sử giao dịch và các nghiệp vụ khác.
- **Thủ thư (MANAGER):**
  - Username: `hoannhunguyen`
  - Password: `123456`
  - Thực hiện tìm kiếm bạn đọc, quản lý mượn/trả sách, báo mất sách, truy vấn đơn mượn và xem thông tin cá nhân.

## 📡 API Endpoints (Theo đúng Báo Cáo Mục 3.1 - 3.6)
- **3.1 Quản lý Khách Hàng:** `/api/KhachHang/**`
- **3.2 Quản lý Sách:** `/api/Sach/**`
- **3.3 Quản lý Đơn Mượn:** `/api/Muon/**`
- **3.4 Quản lý Thể Loại:** `/api/TheLoai/**`
- **3.5 Quản lý Tài Khoản & Đăng Nhập:** `/api/TaiKhoan/**`
- **3.6 Quản lý Lịch Sử Giao Dịch:** `/api/LiSuGiaoDich/**`
- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **H2 Console:** `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:haui_library`, user: `sa`, pass: rỗng)

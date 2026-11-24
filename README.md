# 📚 Hệ Thống Quản Lý Thư Viện HaUI - Trường ĐH Công Nghiệp Hà Nội

Dự án thuộc học phần **Phát triển phần mềm hướng dịch vụ (SOA / Microservices)**.  
Hệ thống được thiết kế và tái cấu trúc thành kiến trúc **Microservices** chuẩn Enterprise với Spring Cloud API Gateway, OpenFeign Inter-Service Communication, React Frontend SPA, Cloud Database Supabase PostgreSQL và Docker Compose.

---

## 🏗️ Kiến Trúc Microservices (Hướng Dịch Vụ)

```
                            ┌────────────────────────────────────────┐
                            │    React Frontend SPA (Port 3000)      │
                            └──────────────────┬─────────────────────┘
                                               │ HTTP / REST
                                               ▼
                            ┌────────────────────────────────────────┐
                            │  Spring Cloud API Gateway (Port 8080)  │
                            └────┬────────────┬───────────┬──────────┘
                                 │            │           │
          ┌──────────────────────┼────────────┴───────────┼─────────────────────┐
          ▼                      ▼                        ▼                     ▼
┌──────────────────┐   ┌──────────────────┐   ┌──────────────────┐   ┌──────────────────┐
│  account-service │   │   book-service   │   │ customer-service │   │  borrow-service  │
│   (Port 8081)    │   │   (Port 8082)    │   │   (Port 8083)    │   │   (Port 8084)    │
│  /api/TaiKhoan   │   │ /api/Sach        │   │  /api/KhachHang  │   │ /api/Muon        │
│                  │   │ /api/TheLoai     │   │                  │   │ /api/LiSuGiaoDich│
└──────────────────┘   └────────▲─────────┘   └────────▲─────────┘   └────────┬─────────┘
                                │                      │                      │
                                └────── OpenFeign ─────┴──────────────────────┘
                                  (Inter-Service Communication)
```

### 1. Phân chia các Dịch vụ (Domain Services)

| STT | Service Name | Port | Chức năng nghiệp vụ & Endpoints |
|---|---|---|---|
| **1** | **gateway-service** | **8080** | Điểm vào duy nhất (Single Entry Point), định tuyến động (Reverse Proxy), cấu hình Global CORS, bảo mật API. |
| **2** | **account-service** | **8081** | Quản lý tài khoản, đăng nhập JWT, phân quyền Admin & Thủ thư (`/api/TaiKhoan/**`). |
| **3** | **book-service** | **8082** | Quản lý danh mục sách, thể loại, tồn kho sách (`/api/Sach/**`, `/api/TheLoai/**`). |
| **4** | **customer-service** | **8083** | Quản lý hồ sơ bạn đọc / độc giả (`/api/KhachHang/**`). |
| **5** | **borrow-service** | **8084** | Nghiệp vụ mượn, trả, báo mất sách, lịch sử giao dịch (`/api/Muon/**`, `/api/LiSuGiaoDich/**`). Kết nối OpenFeign với `book-service` và `customer-service`. |
| **-** | **common-library** | Shared | Thư viện dùng chung: DTOs, Enums, ApiResponse, Exceptions, JWT Provider. |

---

## 🚀 Công Nghệ Sử Dụng

- **Microservices & Backend:** 
  - Java Spring Boot 3.3.5 & Spring Cloud 2023.0.3 (JDK 21/25)
  - Spring Cloud Gateway (Reactive Non-blocking routing)
  - Spring Cloud OpenFeign (Declarative REST Client cho giao tiếp liên dịch vụ)
  - Spring Data JPA & Hibernate 6
  - Spring Security 6 & JWT (`jjwt 0.12.6`), mã hóa BCrypt
- **Cơ sở dữ liệu:**
  - Supabase PostgreSQL Cloud (Profile: `supabase`)
  - H2 Database In-Memory (Profile: `dev`)
- **Frontend:**
  - React 18, Vite 5, Tailwind CSS, Lucide React Icons, Axios
  - Gọi tập trung qua API Gateway (Port 8080)
- **DevOps:**
  - Docker & Docker Compose đa container
  - Maven Multi-module Architecture

---

## 🔑 Tài Khoản Mặc Định Đăng Nhập

| Vai trò | Tên tài khoản | Mật khẩu | Quyền hạn |
|---|---|---|---|
| **Quản trị viên** | `admin` | `123456` | Toàn quyền quản trị danh mục, tài khoản, thống kê giao dịch. |
| **Thủ thư** | `phamquoctoan` | `123456` | Nghiệp vụ mượn sách, trả sách, quản lý độc giả. |

---

## ⚙️ Hướng Dẫn Khởi Chạy Hệ Thống

### Cách 1: Khởi chạy nhanh bằng script Windows (Khuyên dùng)
1. Chạy file `build-microservices.bat` để compile các service (nếu vừa cập nhật code).
2. Chạy file `run-microservices.bat` để tự động bật cả 5 service backend.
3. Chạy frontend:
   ```bash
   cd frontend
   npm run dev
   ```
4. Mở trình duyệt tại: `http://localhost:3000` (hoặc `http://localhost:5173`).

### Cách 2: Khởi chạy bằng Docker Compose (1 Click)
```bash
docker compose up --build
```
Hệ thống sẽ tự khởi động toàn bộ Gateway (8080), 4 Microservices (8081-8084) và Frontend (3000).

### Cách 3: Debug trực tiếp trên VS Code
1. Vào tab **Run & Debug** (Ctrl + Shift + D) trong VS Code.
2. Chọn cấu hình **`▶ Run All Microservices`** và bấm F5.

---

## 📡 Danh Sách API Endpoints (Khớp 100% Báo Cáo Word Bảng 8 - 13)

Tất cả các API được gọi qua Gateway tại `http://localhost:8080`:

1. **Quản lý Khách Hàng (`customer-service` - Port 8083):**
   - `GET /api/KhachHang`: Lấy tất cả khách hàng
   - `GET /api/KhachHang/GetKhachHangById?MaKH=...`: Lấy chi tiết theo mã
   - `GET /api/KhachHang/SearchKhachHangByName?keyword=...`: Tìm theo tên
   - `GET /api/KhachHang/SearchKhachHangBySDT?sdt=...`: Tìm theo SĐT
   - `POST /api/KhachHang/AddKhachHang`: Thêm bạn đọc mới
   - `PUT /api/KhachHang/UpdateKhachHang`: Cập nhật thông tin bạn đọc
   - `DELETE /api/KhachHang/DeleteKhachHang?MaKH=...`: Xóa bạn đọc

2. **Quản lý Sách (`book-service` - Port 8082):**
   - `GET /api/Sach`: Lấy danh sách tất cả đầu sách
   - `GET /api/Sach/GetSachById?MaSach=...`: Chi tiết sách
   - `GET /api/Sach/GetSachByTheLoai?MaTheLoai=...`: Lọc sách theo thể loại
   - `GET /api/Sach/SearchSachByName?keyword=...`: Tìm kiếm sách theo tên
   - `POST /api/Sach/AddSach`: Thêm sách mới (Admin)
   - `PUT /api/Sach/UpdateSach`: Sửa thông tin sách (Admin)
   - `PUT /api/Sach/AddMoreSach`: Nhập thêm số lượng sách (Admin)
   - `PUT /api/Sach/RemoveSomeSach`: Xuất/Hủy bớt số lượng sách (Admin)
   - `DELETE /api/Sach/DeleteSach?MaSach=...`: Xóa đầu sách (Admin)

3. **Quản lý Thể Loại (`book-service` - Port 8082):**
   - `GET /api/TheLoai`: Lấy danh sách thể loại
   - `GET /api/TheLoai/GetTheLoaiById?MaTheLoai=...`: Chi tiết thể loại
   - `POST /api/TheLoai/AddTheLoai`: Thêm thể loại mới (Admin)
   - `PUT /api/TheLoai/UpdateTheLoai`: Sửa tên thể loại (Admin)
   - `DELETE /api/TheLoai/DeleteTheLoai?MaTheLoai=...`: Xóa thể loại (Admin)

4. **Quản lý Mượn / Trả Sách (`borrow-service` - Port 8084):**
   - `GET /api/Muon`: Lấy tất cả phiếu mượn
   - `GET /api/Muon/GetPhieuMuonById?MaPhieu=...`: Chi tiết phiếu mượn
   - `POST /api/Muon/LapPhieuMuon`: Lập phiếu mượn mới (Tự động trừ kho qua OpenFeign)
   - `PUT /api/Muon/TraSach`: Trả sách (Hoàn kho sách qua OpenFeign)
   - `PUT /api/Muon/BaoMatSach`: Báo mất sách (Ghi nhận trạng thái)
   - `DELETE /api/Muon/DeletePhieuMuon?MaPhieu=...`: Xóa phiếu mượn

5. **Lịch Sử Giao Dịch (`borrow-service` - Port 8084):**
   - `GET /api/LiSuGiaoDich`: Danh sách toàn bộ lịch sử giao dịch
   - `GET /api/LiSuGiaoDich/GetLSuGiaoDichByUsername?Username=...`: Lịch sử theo người thao tác
   - `POST /api/LiSuGiaoDich/AddLiSuGiaoDich`: Ghi nhận giao dịch mới

6. **Quản lý Tài Khoản & Phân Quyền (`account-service` - Port 8081):**
   - `POST /api/TaiKhoan/Login`: Xác thực tài khoản, trả về JWT Token & thông tin người dùng
   - `GET /api/TaiKhoan`: Danh sách tài khoản (Admin)
   - `GET /api/TaiKhoan/GetTaiKhoanByUsername?username=...`: Chi tiết tài khoản
   - `POST /api/TaiKhoan/AddTaiKhoan`: Tạo tài khoản mới (Admin)
   - `PUT /api/TaiKhoan/UpdateTaiKhoan`: Cập nhật tài khoản
   - `DELETE /api/TaiKhoan/DeleteTaiKhoan?Username=...`: Xóa tài khoản (Admin)

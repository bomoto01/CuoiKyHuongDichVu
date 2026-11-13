# 📚 Hệ Thống Quản Lý Thư Viện HaUI - Trường ĐH Công Nghiệp Hà Nội

Dự án thuộc học phần **Phát triển phần mềm hướng dịch vụ (SOA) - Nhóm 18**.  
Xây dựng hệ thống quản lý thư viện hiện đại với kiến trúc phân tầng chuẩn Enterprise (Backend REST API + Frontend SPA + Cơ sở dữ liệu Cloud + Đóng gói Docker).

---

## 🚀 Công Nghệ Sử Dụng

- **Backend:** 
  - Java Spring Boot 3.3.5 (JDK 21/25)
  - Spring Data JPA (Hibernate 6, tối ưu truy vấn `JOIN FETCH` triệt tiêu N+1 Query)
  - Spring Security 6 & JWT (`jjwt 0.12.6`), mật khẩu mã hóa BCrypt
  - MapStruct 1.6.3 (High-performance DTO mapping)
  - HikariCP (Connection Pool giữ kết nối ấm)
  - Swagger / OpenAPI 3 UI
- **Database:**
  - **Cloud:** Supabase PostgreSQL Cloud (Profile: `supabase`)
  - **Local/Test:** H2 In-Memory (Profile: `dev`) & SQL Server (Profile: `sqlserver`)
- **Frontend:**
  - React 18, Vite 5, Tailwind CSS
  - Lucide React Icons, Axios
  - Kiến trúc Instant Tab Switching (0ms latency)
- **DevOps & Triển Khai:**
  - Docker & Docker Compose (Multi-stage build)
  - Triển khai Backend trên **Render**
  - Triển khai Frontend trên **Vercel**

---

## 🔑 Tài Khoản Mặc Định Đăng Nhập Hệ Thống

| Vai trò | Tên tài khoản (Username) | Mật khẩu (Password) | Quyền hạn & Chức năng |
|---|---|---|---|
| **Quản trị viên (ADMIN)** | `admin` | `123456` | Toàn quyền: Quản lý Danh mục Sách, Thể loại, Tài khoản nhân sự, Lịch sử giao dịch và phân quyền. |
| **Thủ thư (MANAGER)** | `phamquoctoan` | `123456` | Nghiệp vụ thư viện: Tra cứu bạn đọc, Mượn sách, Trả/Mất sách, Truy vấn đơn mượn, Quản lý bạn đọc. |

*(Trên màn hình Đăng nhập có 2 nút chọn nhanh tài khoản giúp giảng viên và người chấm bài test hệ thống nhanh chóng).*

---

## 📂 Cấu Trúc Dự Án

```
├── backend/
│   ├── src/main/java/com/haui/library/
│   │   ├── config/             # SecurityConfig, CORS, ApplicationInitConfig (Seed Data)
│   │   ├── controller/         # 6 Nhóm REST Controller theo đặc tả
│   │   ├── dto/                # Request & Response DTOs
│   │   ├── entity/             # 6 JPA Entities (TheLoai, Sach, KhachHang, TaiKhoan, PhieuMuon, LichSuGiaoDich)
│   │   ├── enums/              # Role (ADMIN, MANAGER), TransactionStatus
│   │   ├── exception/          # GlobalExceptionHandler, AppException, ErrorCode
│   │   ├── mapper/             # MapStruct Mappers
│   │   ├── repository/         # Spring Data JPA Repositories
│   │   ├── security/           # JwtTokenProvider, CustomUserDetailsService, JwtAuthenticationFilter
│   │   └── service/            # Service Interfaces & Implementations
│   ├── src/main/resources/
│   │   └── application.yml     # Cấu hình đa profile (supabase, dev, sqlserver)
│   ├── Dockerfile
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── api/                # Axios Client & API Services
│   │   ├── components/         # Navbar, Modal, Toast
│   │   ├── context/            # AuthContext (JWT Authentication State)
│   │   └── pages/              # Login, AdminDashboard, ManagerDashboard
│   ├── Dockerfile
│   ├── nginx.conf
│   └── vercel.json
├── docker-compose.yml
├── Dockerfile                  # Root Dockerfile cho Render
├── render.yaml                 # Blueprint cho Render
└── vercel.json                 # Cấu hình SPA Rewrite cho Vercel
```

---

## 📡 Danh Sách API Endpoints (Theo đúng Báo Cáo Mục 3.1 - 3.6)

1. **3.1 Quản lý Khách Hàng (Bạn đọc):**
   - `GET /api/KhachHang`: Lấy tất cả khách hàng
   - `GET /api/KhachHang/GetKhachHangById?MaKH=...`: Lấy chi tiết theo mã
   - `GET /api/KhachHang/SearchKhachHangByName?keyword=...`: Tìm theo tên
   - `GET /api/KhachHang/SearchKhachHangBySDT?sdt=...`: Tìm theo SĐT
   - `POST /api/KhachHang/AddKhachHang`: Thêm bạn đọc mới
   - `PUT /api/KhachHang/UpdateKhachHang`: Cập nhật thông tin bạn đọc
   - `DELETE /api/KhachHang/DeleteKhachHang?MaKH=...`: Xóa bạn đọc

2. **3.2 Quản lý Sách:**
   - `GET /api/Sach`: Lấy danh sách tất cả đầu sách
   - `GET /api/Sach/GetSachById?MaSach=...`: Chi tiết sách
   - `GET /api/Sach/GetSachByTheLoai?MaTheLoai=...`: Lọc sách theo thể loại
   - `GET /api/Sach/SearchSachByName?keyword=...`: Tìm kiếm sách theo tên
   - `POST /api/Sach/AddSach`: Thêm sách mới (Admin)
   - `PUT /api/Sach/UpdateSach`: Sửa thông tin sách (Admin)
   - `PUT /api/Sach/AddMoreSach`: Nhập thêm số lượng sách (Admin)
   - `PUT /api/Sach/RemoveSomeSach`: Xuất/Hủy bớt số lượng sách (Admin)
   - `DELETE /api/Sach/DeleteSach?MaSach=...`: Xóa đầu sách (Admin)

3. **3.3 Quản lý Mượn/Trả Sách:**
   - `GET /api/Muon/GetPhieuMuon`: Lấy toàn bộ danh sách phiếu mượn
   - `GET /api/Muon/GetPhieuMuonByMaKH?MaKH=...`: Phiếu mượn theo bạn đọc
   - `GET /api/Muon/GetPhieuMuonByMaSach?MaSach=...`: Phiếu mượn theo mã sách
   - `GET /api/Muon/GetPhieuMuonQuaHan`: Danh sách sách quá hạn trả
   - `POST /api/Muon/TaoPhieuMuon`: Lập phiếu mượn mới
   - `POST /api/Muon/TraSach`: Xác nhận trả sách
   - `POST /api/Muon/BaoMatSach`: Báo mất sách (ghi nhận trạng thái)

4. **3.4 Quản lý Thể Loại:**
   - `GET /api/TheLoai`: Lấy danh sách thể loại
   - `GET /api/TheLoai/GetTheLoaiById?MaTheLoai=...`: Chi tiết thể loại
   - `GET /api/TheLoai/SearchTheLoaiByName?keyword=...`: Tìm kiếm thể loại
   - `POST /api/TheLoai/AddTheLoai`: Thêm thể loại (Admin)
   - `PUT /api/TheLoai/UpdateTheLoai`: Sửa thể loại (Admin)
   - `DELETE /api/TheLoai/DeleteTheLoai?MaTheLoai=...`: Xóa thể loại (Admin)

5. **3.5 Quản lý Tài Khoản:**
   - `POST /api/TaiKhoan/Login`: Đăng nhập cấp JWT Token
   - `GET /api/TaiKhoan/my-info`: Lấy thông tin tài khoản hiện tại
   - `GET /api/TaiKhoan`: Danh sách tài khoản (Admin)
   - `POST /api/TaiKhoan/AddTaiKhoan`: Thêm tài khoản thủ thư (Admin)
   - `PUT /api/TaiKhoan/UpdateTaiKhoan`: Cập nhật tài khoản (Admin)
   - `DELETE /api/TaiKhoan/DeleteTaiKhoan?username=...`: Xóa tài khoản (Admin)

6. **3.6 Lịch Sử Giao Dịch:**
   - `GET /api/LiSuGiaoDich`: Xem toàn bộ lịch sử mượn/trả
   - `GET /api/LiSuGiaoDich/GetByUsername?username=...`: Lọc theo thủ thư thực hiện
   - `GET /api/LiSuGiaoDich/GetByMaSach?maSach=...`: Lọc theo sách
   - `GET /api/LiSuGiaoDich/GetByNgayGD`: Lọc theo khoảng ngày
   - `DELETE /api/LiSuGiaoDich/DeleteBeforeDate`: Dọn dẹp nhật ký cũ

---

## 🛠️ Hướng Dẫn Cài Đặt & Khởi Chạy

### Cách 1: Chạy trực tiếp trên máy (Development)

1. **Khởi động Backend:**
   ```powershell
   java -jar backend/target/library-management-1.0.0.jar
   ```
   *(Backend chạy tại `http://localhost:8080`, Swagger UI tại `http://localhost:8080/swagger-ui/index.html`)*

2. **Khởi động Frontend:**
   ```powershell
   cd frontend
   npm install
   npm run dev
   ```
   *(Frontend chạy tại `http://localhost:5173`)*

---

### Cách 2: Chạy toàn bộ bằng Docker Compose (Khuyên dùng)

Chỉ cần cài đặt Docker Desktop và chạy lệnh tại thư mục gốc dự án:
```powershell
docker-compose up -d --build
```
- **Giao diện Web:** `http://localhost:3000`
- **Backend API:** `http://localhost:8080`

---

## 🌐 Triển Khai Lên Cloud (Production)

- **Backend:** Triển khai qua Docker Web Service trên **Render** (sử dụng [Dockerfile](Dockerfile)).
- **Frontend:** Triển khai qua **Vercel** (kết nối trực tiếp thư mục `frontend/`, cấu hình biến môi trường `VITE_API_URL` trỏ về Backend Render).

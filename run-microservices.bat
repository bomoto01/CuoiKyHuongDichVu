@echo off
chcp 65001 >nul
echo =====================================================================
echo    HE THONG THU VIEN HAUI - KHOI DONG HE THONG MICROSERVICES
echo =====================================================================
echo 1. Gateway Service  : http://localhost:8080 (Diem vao duy nhat)
echo 2. Account Service  : http://localhost:8081 (/api/TaiKhoan)
echo 3. Book Service     : http://localhost:8082 (/api/Sach, /api/TheLoai)
echo 4. Customer Service : http://localhost:8083 (/api/KhachHang)
echo 5. Borrow Service   : http://localhost:8084 (/api/Muon, /api/LiSuGiaoDich)
echo =====================================================================

cd /d "%~dp0"

:: Load bien moi truong tu file .env neu co
if exist "%~dp0\.env" (
    for /f "usebackq tokens=1* delims==" %%i in ("%~dp0\.env") do (
        set "%%i=%%j"
    )
)
if "%SUPABASE_DB_PASSWORD%"=="" set "SUPABASE_DB_PASSWORD=monnayphaiduocA"
if "%DB_PASSWORD%"=="" set "DB_PASSWORD=monnayphaiduocA"

cd /d "%~dp0\backend"

:: Kiem tra neu chua build JAR thi thong bao
if not exist "gateway-service\target\gateway-service-1.0.0.jar" (
    echo [CANH BAO] Chua thay file JAR. Dang tien hanh build tu dong...
    call mvn clean package -DskipTests
)

echo [1/5] Dang khoi dong Account Service (Port 8081)...
start "Account-Service [8081]" cmd /k "title Account-Service [8081] && java -Dfile.encoding=UTF-8 -jar account-service\target\account-service-1.0.0.jar"
timeout /t 3 /nobreak >nul

echo [2/5] Dang khoi dong Book Service (Port 8082)...
start "Book-Service [8082]" cmd /k "title Book-Service [8082] && java -Dfile.encoding=UTF-8 -jar book-service\target\book-service-1.0.0.jar"
timeout /t 3 /nobreak >nul

echo [3/5] Dang khoi dong Customer Service (Port 8083)...
start "Customer-Service [8083]" cmd /k "title Customer-Service [8083] && java -Dfile.encoding=UTF-8 -jar customer-service\target\customer-service-1.0.0.jar"
timeout /t 3 /nobreak >nul

echo [4/5] Dang khoi dong Borrow Service (Port 8084)...
start "Borrow-Service [8084]" cmd /k "title Borrow-Service [8084] && java -Dfile.encoding=UTF-8 -jar borrow-service\target\borrow-service-1.0.0.jar"
timeout /t 4 /nobreak >nul

echo [5/5] Dang khoi dong API Gateway (Port 8080)...
start "Gateway-Service [8080]" cmd /k "title Gateway-Service [8080] && java -Dfile.encoding=UTF-8 -jar gateway-service\target\gateway-service-1.0.0.jar"

echo =====================================================================
echo TAT CA CAC SERVICE DA DUOC KHOI DONG THANH CONG!
echo Frontend truy cap tai: http://localhost:3000 (ket noi qua Gateway 8080)
echo =====================================================================
pause

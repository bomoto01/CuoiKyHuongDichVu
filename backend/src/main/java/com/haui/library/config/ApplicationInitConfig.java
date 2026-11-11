package com.haui.library.config;

import com.haui.library.entity.*;
import com.haui.library.enums.Role;
import com.haui.library.enums.TransactionStatus;
import com.haui.library.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner applicationRunner(
            TaiKhoanRepository taiKhoanRepository,
            TheLoaiRepository theLoaiRepository,
            SachRepository sachRepository,
            KhachHangRepository khachHangRepository,
            PhieuMuonRepository phieuMuonRepository,
            LichSuGiaoDichRepository lichSuGiaoDichRepository
    ) {
        return args -> {
            log.info("Starting initial database seeding for HaUI Library Management System...");

            // 1. Seed Accounts
            if (taiKhoanRepository.count() == 0) {
                TaiKhoan admin = TaiKhoan.builder()
                        .username("admin")
                        .pass(passwordEncoder.encode("123456"))
                        .tenTK("Quản Trị Viên (Admin)")
                        .sdtTK("0347162755")
                        .role(Role.ADMIN)
                        .build();

                TaiKhoan manager = TaiKhoan.builder()
                        .username("hoannhunguyen")
                        .pass(passwordEncoder.encode("123456"))
                        .tenTK("Nguyễn Như Hoàn (Thủ Thư)")
                        .sdtTK("0347162755")
                        .role(Role.MANAGER)
                        .build();

                taiKhoanRepository.save(admin);
                taiKhoanRepository.save(manager);
                log.info("Default accounts created: admin/123456, hoannhunguyen/123456");
            }

            // 2. Seed Categories
            if (theLoaiRepository.count() == 0) {
                TheLoai tl1 = TheLoai.builder().maTheLoai("TL001").tenTheLoai("Khoa học").build();
                TheLoai tl2 = TheLoai.builder().maTheLoai("TL002").tenTheLoai("Văn học").build();
                TheLoai tl3 = TheLoai.builder().maTheLoai("TL003").tenTheLoai("Giải trí").build();
                TheLoai tl4 = TheLoai.builder().maTheLoai("TL004").tenTheLoai("Công nghệ thông tin").build();

                theLoaiRepository.save(tl1);
                theLoaiRepository.save(tl2);
                theLoaiRepository.save(tl3);
                theLoaiRepository.save(tl4);
            }

            // 3. Seed Books
            if (sachRepository.count() == 0) {
                TheLoai tl1 = theLoaiRepository.findById("TL001").orElse(null);
                TheLoai tl2 = theLoaiRepository.findById("TL002").orElse(null);
                TheLoai tl3 = theLoaiRepository.findById("TL003").orElse(null);
                TheLoai tl4 = theLoaiRepository.findById("TL004").orElse(null);

                Sach s1 = Sach.builder().maSach("S001").tenSach("Toán lớp 10").tacGia("Huỳnh Nhật Minh").soLuong(9).theLoai(tl1).build();
                Sach s2 = Sach.builder().maSach("S002").tenSach("Vì sao lại thế").tacGia("Hoàng Mai").soLuong(5).theLoai(tl3).build();
                Sach s3 = Sach.builder().maSach("S003").tenSach("Sao tháng tám").tacGia("Nguyễn Nhật Ánh").soLuong(10).theLoai(tl2).build();
                Sach s4 = Sach.builder().maSach("S004").tenSach("Văn lớp 3").tacGia("Như Hoàn").soLuong(20).theLoai(tl2).build();
                Sach s5 = Sach.builder().maSach("S005").tenSach("Lập trình Web Spring Boot & React").tacGia("Trần Thanh Hùng").soLuong(15).theLoai(tl4).build();

                sachRepository.save(s1);
                sachRepository.save(s2);
                sachRepository.save(s3);
                sachRepository.save(s4);
                sachRepository.save(s5);
            }

            // 4. Seed Customers
            if (khachHangRepository.count() == 0) {
                KhachHang kh1 = KhachHang.builder().maKH("KH125583").tenKH("Nguyen Van B").sdtKH("0987654321").build();
                KhachHang kh2 = KhachHang.builder().maKH("KH252642").tenKH("Huỳnh Nhật Minh").sdtKH("0123456789").build();
                KhachHang kh3 = KhachHang.builder().maKH("KH831056").tenKH("Nguyễn Như Hoàn").sdtKH("0347162755").build();

                khachHangRepository.save(kh1);
                khachHangRepository.save(kh2);
                khachHangRepository.save(kh3);
            }

            // 5. Seed Sample Borrow slips & Transaction History
            if (phieuMuonRepository.count() == 0) {
                KhachHang kh2 = khachHangRepository.findById("KH252642").orElse(null);
                KhachHang kh1 = khachHangRepository.findById("KH125583").orElse(null);
                Sach s1 = sachRepository.findById("S001").orElse(null);
                Sach s3 = sachRepository.findById("S003").orElse(null);
                TaiKhoan manager = taiKhoanRepository.findById("hoannhunguyen").orElse(null);

                if (kh2 != null && s1 != null) {
                    PhieuMuon pm1 = PhieuMuon.builder()
                            .maMuon("PM001")
                            .khachHang(kh2)
                            .sach(s1)
                            .soLuong(2)
                            .ngayMuon(LocalDateTime.now().minusDays(3))
                            .ngayTra(LocalDateTime.now().plusDays(11))
                            .build();
                    phieuMuonRepository.save(pm1);

                    LichSuGiaoDich lsgd1 = LichSuGiaoDich.builder()
                            .maGD("GD100001")
                            .ngayGD(LocalDateTime.now().minusDays(3))
                            .soLuong(2)
                            .trangThai(TransactionStatus.MUON.getDescription())
                            .sach(s1)
                            .khachHang(kh2)
                            .taiKhoan(manager)
                            .build();
                    lichSuGiaoDichRepository.save(lsgd1);
                }

                if (kh1 != null && s3 != null) {
                    // Overdue slip for testing "Quá hạn trả"
                    PhieuMuon pm2 = PhieuMuon.builder()
                            .maMuon("PM002")
                            .khachHang(kh1)
                            .sach(s3)
                            .soLuong(1)
                            .ngayMuon(LocalDateTime.now().minusDays(20))
                            .ngayTra(LocalDateTime.now().minusDays(6))
                            .build();
                    phieuMuonRepository.save(pm2);

                    LichSuGiaoDich lsgd2 = LichSuGiaoDich.builder()
                            .maGD("GD100002")
                            .ngayGD(LocalDateTime.now().minusDays(20))
                            .soLuong(1)
                            .trangThai(TransactionStatus.MUON.getDescription())
                            .sach(s3)
                            .khachHang(kh1)
                            .taiKhoan(manager)
                            .build();
                    lichSuGiaoDichRepository.save(lsgd2);
                }
            }

            log.info("HaUI Library initial data seeding completed successfully.");
        };
    }
}

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
import java.util.List;

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
            LichSuGiaoDichRepository lichSuGiaoDichRepository) {
        return args -> {
            log.info("Starting initial database seeding for HaUI Library Management System...");

            // 1. Seed Accounts
            if (!taiKhoanRepository.existsById("admin")) {
                TaiKhoan admin = TaiKhoan.builder()
                        .username("admin")
                        .pass(passwordEncoder.encode("123456"))
                        .tenTK("Quản Trị Viên (Admin)")
                        .sdtTK("0347162755")
                        .role(Role.ADMIN)
                        .build();
                taiKhoanRepository.save(admin);
                log.info("Default admin account created: admin/123456");
            }

            if (!taiKhoanRepository.existsById("phamquoctoan")) {
                TaiKhoan manager = TaiKhoan.builder()
                        .username("phamquoctoan")
                        .pass(passwordEncoder.encode("123456"))
                        .tenTK("Phạm Quốc Toản (Thủ Thư)")
                        .sdtTK("0347162755")
                        .role(Role.MANAGER)
                        .build();
                taiKhoanRepository.save(manager);
                log.info("Default manager account created: phamquoctoan/123456");
            }

            // Đồng bộ dữ liệu: xóa tài khoản cũ hoannhunguyen nếu còn tồn tại
            if (taiKhoanRepository.existsById("hoannhunguyen")) {
                TaiKhoan toan = taiKhoanRepository.findById("phamquoctoan").orElse(null);
                if (toan != null) {
                    var txList = lichSuGiaoDichRepository.findByTaiKhoan_Username("hoannhunguyen");
                    for (var tx : txList) {
                        tx.setTaiKhoan(toan);
                        lichSuGiaoDichRepository.save(tx);
                    }
                }
                taiKhoanRepository.deleteById("hoannhunguyen");
                log.info("Old account hoannhunguyen removed and transactions migrated to phamquoctoan.");
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

                Sach s1 = Sach.builder().maSach("S001").tenSach("Toán lớp 10").tacGia("Huỳnh Nhật Minh").soLuong(9)
                        .theLoai(tl1).build();
                Sach s2 = Sach.builder().maSach("S002").tenSach("Vì sao lại thế").tacGia("Hoàng Mai").soLuong(5)
                        .theLoai(tl3).build();
                Sach s3 = Sach.builder().maSach("S003").tenSach("Sao tháng tám").tacGia("Nguyễn Nhật Ánh").soLuong(10)
                        .theLoai(tl2).build();
                Sach s4 = Sach.builder().maSach("S004").tenSach("Văn lớp 3").tacGia("Như Hoàn").soLuong(20).theLoai(tl2)
                        .build();
                Sach s5 = Sach.builder().maSach("S005").tenSach("Lập trình Web Spring Boot & React")
                        .tacGia("Trần Thanh Hùng").soLuong(15).theLoai(tl4).build();

                sachRepository.save(s1);
                sachRepository.save(s2);
                sachRepository.save(s3);
                sachRepository.save(s4);
                sachRepository.save(s5);
            }

            // 4. Seed Customers
            if (khachHangRepository.count() == 0) {
                KhachHang kh1 = KhachHang.builder().maKH("KH125583").tenKH("Nguyen Van B").sdtKH("0987654321").build();
                KhachHang kh2 = KhachHang.builder().maKH("KH252642").tenKH("Huỳnh Nhật Minh").sdtKH("0123456789")
                        .build();
                KhachHang kh3 = KhachHang.builder().maKH("KH831056").tenKH("Nguyễn Như Hoàn").sdtKH("0347162755")
                        .build();

                khachHangRepository.save(kh1);
                khachHangRepository.save(kh2);
                khachHangRepository.save(kh3);
            }

            // 5. Clean up sample borrow slips & transactions for Phạm Quốc Toản & Bùi Minh Tâm
            try {
                List<String> targetIds = List.of("KH125583", "KH252642");
                List<String> targetNames = List.of("Bùi Minh Tâm", "Phạm Quốc Toản", "Nguyen Van B", "Huỳnh Nhật Minh");

                List<PhieuMuon> sampleBorrows = phieuMuonRepository.findAll().stream()
                        .filter(pm -> (pm.getKhachHang() != null && (
                                targetIds.contains(pm.getKhachHang().getMaKH()) ||
                                (pm.getKhachHang().getTenKH() != null && targetNames.stream().anyMatch(n -> pm.getKhachHang().getTenKH().equalsIgnoreCase(n)))
                        )) || "PM001".equalsIgnoreCase(pm.getMaMuon()) || "PM002".equalsIgnoreCase(pm.getMaMuon()))
                        .toList();

                if (!sampleBorrows.isEmpty()) {
                    for (PhieuMuon pm : sampleBorrows) {
                        Sach book = pm.getSach();
                        if (book != null && pm.getSoLuong() != null) {
                            book.setSoLuong(book.getSoLuong() + pm.getSoLuong());
                            sachRepository.save(book);
                        }
                    }
                    phieuMuonRepository.deleteAll(sampleBorrows);
                    log.info("Da xoa {} phieu muon mau cua Pham Quoc Toan va Bui Minh Tam.", sampleBorrows.size());
                }

                List<LichSuGiaoDich> sampleTx = lichSuGiaoDichRepository.findAll().stream()
                        .filter(tx -> (tx.getKhachHang() != null && (
                                targetIds.contains(tx.getKhachHang().getMaKH()) ||
                                (tx.getKhachHang().getTenKH() != null && targetNames.stream().anyMatch(n -> tx.getKhachHang().getTenKH().equalsIgnoreCase(n)))
                        )) || "GD100001".equalsIgnoreCase(tx.getMaGD()) || "GD100002".equalsIgnoreCase(tx.getMaGD()))
                        .toList();

                if (!sampleTx.isEmpty()) {
                    lichSuGiaoDichRepository.deleteAll(sampleTx);
                    log.info("Da xoa {} ban ghi lich su giao dich mau.", sampleTx.size());
                }
            } catch (Exception ex) {
                log.warn("Loi khi don dep du lieu mau: {}", ex.getMessage());
            }

            log.info("HaUI Library initial data seeding completed successfully.");
        };
    }
}

package com.haui.library.repository;

import com.haui.library.entity.PhieuMuon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PhieuMuonRepository extends JpaRepository<PhieuMuon, String> {
    List<PhieuMuon> findByKhachHang_MaKH(String maKH);
    List<PhieuMuon> findBySach_MaSach(String maSach);
    List<PhieuMuon> findByNgayMuonBetween(LocalDateTime start, LocalDateTime end);
    List<PhieuMuon> findByNgayTraBefore(LocalDateTime now);
    boolean existsByKhachHang_MaKH(String maKH);
    boolean existsBySach_MaSach(String maSach);
}

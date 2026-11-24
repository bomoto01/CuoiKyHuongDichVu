package com.haui.borrow.repository;

import com.haui.borrow.entity.LichSuGiaoDich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LichSuGiaoDichRepository extends JpaRepository<LichSuGiaoDich, String> {

    List<LichSuGiaoDich> findByUsername(String username);

    List<LichSuGiaoDich> findByMaSach(String maSach);

    List<LichSuGiaoDich> findByNgayGDBetween(LocalDateTime start, LocalDateTime end);

    List<LichSuGiaoDich> findByTrangThai(String trangThai);

    int deleteByNgayGDBefore(LocalDateTime cutOff);
}

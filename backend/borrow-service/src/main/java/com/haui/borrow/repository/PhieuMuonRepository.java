package com.haui.borrow.repository;

import com.haui.borrow.entity.PhieuMuon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PhieuMuonRepository extends JpaRepository<PhieuMuon, String> {

    List<PhieuMuon> findByMaKH(String maKH);

    List<PhieuMuon> findByMaSach(String maSach);

    List<PhieuMuon> findByNgayMuonBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT p FROM PhieuMuon p WHERE p.ngayTra < :now")
    List<PhieuMuon> findByNgayTraBefore(@Param("now") LocalDateTime now);
}

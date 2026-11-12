package com.haui.library.repository;

import com.haui.library.entity.PhieuMuon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PhieuMuonRepository extends JpaRepository<PhieuMuon, String> {

    @Query("SELECT pm FROM PhieuMuon pm LEFT JOIN FETCH pm.khachHang LEFT JOIN FETCH pm.sach s LEFT JOIN FETCH s.theLoai")
    List<PhieuMuon> findAll();

    @Query("SELECT pm FROM PhieuMuon pm LEFT JOIN FETCH pm.khachHang LEFT JOIN FETCH pm.sach s LEFT JOIN FETCH s.theLoai WHERE pm.khachHang.maKH = :maKH")
    List<PhieuMuon> findByKhachHang_MaKH(@Param("maKH") String maKH);

    @Query("SELECT pm FROM PhieuMuon pm LEFT JOIN FETCH pm.khachHang LEFT JOIN FETCH pm.sach s LEFT JOIN FETCH s.theLoai WHERE pm.sach.maSach = :maSach")
    List<PhieuMuon> findBySach_MaSach(@Param("maSach") String maSach);

    @Query("SELECT pm FROM PhieuMuon pm LEFT JOIN FETCH pm.khachHang LEFT JOIN FETCH pm.sach s LEFT JOIN FETCH s.theLoai WHERE pm.ngayMuon BETWEEN :start AND :end")
    List<PhieuMuon> findByNgayMuonBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT pm FROM PhieuMuon pm LEFT JOIN FETCH pm.khachHang LEFT JOIN FETCH pm.sach s LEFT JOIN FETCH s.theLoai WHERE pm.ngayTra < :now")
    List<PhieuMuon> findByNgayTraBefore(@Param("now") LocalDateTime now);

    boolean existsByKhachHang_MaKH(String maKH);
    boolean existsBySach_MaSach(String maSach);
}

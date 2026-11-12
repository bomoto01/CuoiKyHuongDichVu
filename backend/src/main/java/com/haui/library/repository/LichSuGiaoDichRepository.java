package com.haui.library.repository;

import com.haui.library.entity.LichSuGiaoDich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LichSuGiaoDichRepository extends JpaRepository<LichSuGiaoDich, String> {

    @Query("SELECT l FROM LichSuGiaoDich l LEFT JOIN FETCH l.taiKhoan LEFT JOIN FETCH l.khachHang LEFT JOIN FETCH l.sach s LEFT JOIN FETCH s.theLoai ORDER BY l.ngayGD DESC")
    List<LichSuGiaoDich> findAll();

    @Query("SELECT l FROM LichSuGiaoDich l LEFT JOIN FETCH l.taiKhoan LEFT JOIN FETCH l.khachHang LEFT JOIN FETCH l.sach s LEFT JOIN FETCH s.theLoai WHERE l.taiKhoan.username = :username ORDER BY l.ngayGD DESC")
    List<LichSuGiaoDich> findByTaiKhoan_Username(@Param("username") String username);

    @Query("SELECT l FROM LichSuGiaoDich l LEFT JOIN FETCH l.taiKhoan LEFT JOIN FETCH l.khachHang LEFT JOIN FETCH l.sach s LEFT JOIN FETCH s.theLoai WHERE l.sach.maSach = :maSach ORDER BY l.ngayGD DESC")
    List<LichSuGiaoDich> findBySach_MaSach(@Param("maSach") String maSach);

    @Query("SELECT l FROM LichSuGiaoDich l LEFT JOIN FETCH l.taiKhoan LEFT JOIN FETCH l.khachHang LEFT JOIN FETCH l.sach s LEFT JOIN FETCH s.theLoai WHERE l.ngayGD BETWEEN :start AND :end ORDER BY l.ngayGD DESC")
    List<LichSuGiaoDich> findByNgayGDBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT l FROM LichSuGiaoDich l LEFT JOIN FETCH l.taiKhoan LEFT JOIN FETCH l.khachHang LEFT JOIN FETCH l.sach s LEFT JOIN FETCH s.theLoai WHERE l.trangThai = :trangThai ORDER BY l.ngayGD DESC")
    List<LichSuGiaoDich> findByTrangThai(@Param("trangThai") String trangThai);

    @Modifying
    @Query("DELETE FROM LichSuGiaoDich l WHERE l.ngayGD < :cutOffDate")
    int deleteByNgayGDBefore(@Param("cutOffDate") LocalDateTime cutOffDate);
}

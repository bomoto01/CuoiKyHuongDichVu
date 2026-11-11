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
    List<LichSuGiaoDich> findByTaiKhoan_Username(String username);
    List<LichSuGiaoDich> findBySach_MaSach(String maSach);
    List<LichSuGiaoDich> findByNgayGDBetween(LocalDateTime start, LocalDateTime end);
    List<LichSuGiaoDich> findByTrangThai(String trangThai);
    
    @Modifying
    @Query("DELETE FROM LichSuGiaoDich l WHERE l.ngayGD < :cutOffDate")
    int deleteByNgayGDBefore(@Param("cutOffDate") LocalDateTime cutOffDate);
}


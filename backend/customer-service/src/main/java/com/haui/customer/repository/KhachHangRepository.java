package com.haui.customer.repository;

import com.haui.customer.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, String> {

    @Query("SELECT k FROM KhachHang k WHERE LOWER(k.tenKH) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<KhachHang> searchByName(@Param("keyword") String keyword);

    @Query("SELECT k FROM KhachHang k WHERE k.sdtKH LIKE CONCAT('%', :sdt, '%')")
    List<KhachHang> searchBySDT(@Param("sdt") String sdt);
}

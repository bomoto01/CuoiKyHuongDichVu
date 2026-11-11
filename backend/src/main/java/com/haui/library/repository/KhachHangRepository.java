package com.haui.library.repository;

import com.haui.library.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, String> {
    List<KhachHang> findByTenKHContainingIgnoreCase(String keyword);
    List<KhachHang> findBySdtKHContaining(String sdt);
    Optional<KhachHang> findBySdtKH(String sdt);
    boolean existsByMaKH(String maKH);
}

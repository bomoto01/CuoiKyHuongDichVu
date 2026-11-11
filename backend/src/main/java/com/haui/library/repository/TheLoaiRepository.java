package com.haui.library.repository;

import com.haui.library.entity.TheLoai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheLoaiRepository extends JpaRepository<TheLoai, String> {
    List<TheLoai> findByTenTheLoaiContainingIgnoreCase(String tenTheLoai);
    boolean existsByMaTheLoai(String maTheLoai);
}

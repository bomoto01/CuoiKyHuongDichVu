package com.haui.library.repository;

import com.haui.library.entity.Sach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SachRepository extends JpaRepository<Sach, String> {
    List<Sach> findByTheLoai_MaTheLoai(String maTheLoai);
    List<Sach> findByTenSachContainingIgnoreCase(String keyword);
    boolean existsByTheLoai_MaTheLoai(String maTheLoai);
}


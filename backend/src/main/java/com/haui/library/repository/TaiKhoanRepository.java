package com.haui.library.repository;

import com.haui.library.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {
    Optional<TaiKhoan> findByUsername(String username);
    List<TaiKhoan> findByUsernameContainingIgnoreCaseOrTenTKContainingIgnoreCase(String username, String tenTK);
    boolean existsByUsername(String username);
}

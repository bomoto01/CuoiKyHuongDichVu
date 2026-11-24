package com.haui.account.repository;

import com.haui.account.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {
    Optional<TaiKhoan> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("SELECT t FROM TaiKhoan t WHERE LOWER(t.tenTK) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.username) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<TaiKhoan> searchByNameOrUsername(@Param("keyword") String keyword);
}

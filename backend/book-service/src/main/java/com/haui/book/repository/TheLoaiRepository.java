package com.haui.book.repository;

import com.haui.book.entity.TheLoai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheLoaiRepository extends JpaRepository<TheLoai, String> {
    @Query("SELECT t FROM TheLoai t WHERE LOWER(t.tenTheLoai) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<TheLoai> searchByName(@Param("keyword") String keyword);
}

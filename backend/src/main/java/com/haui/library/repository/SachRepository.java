package com.haui.library.repository;

import com.haui.library.entity.Sach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SachRepository extends JpaRepository<Sach, String> {

    @Query("SELECT s FROM Sach s LEFT JOIN FETCH s.theLoai")
    List<Sach> findAll();

    @Query("SELECT s FROM Sach s LEFT JOIN FETCH s.theLoai WHERE s.maSach = :maSach")
    Optional<Sach> findByIdWithTheLoai(@Param("maSach") String maSach);

    @Query("SELECT s FROM Sach s LEFT JOIN FETCH s.theLoai WHERE s.theLoai.maTheLoai = :maTheLoai")
    List<Sach> findByTheLoai_MaTheLoai(@Param("maTheLoai") String maTheLoai);

    @Query("SELECT s FROM Sach s LEFT JOIN FETCH s.theLoai WHERE LOWER(s.tenSach) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Sach> findByTenSachContainingIgnoreCase(@Param("keyword") String keyword);

    boolean existsByTheLoai_MaTheLoai(String maTheLoai);
}

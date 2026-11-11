package com.haui.library.service;

import com.haui.library.dto.response.LichSuGiaoDichResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface LichSuGiaoDichService {
    List<LichSuGiaoDichResponse> getAll();
    List<LichSuGiaoDichResponse> getByUsername(String username);
    List<LichSuGiaoDichResponse> getByMaSach(String maSach);
    List<LichSuGiaoDichResponse> getByNgayGD(LocalDateTime ngayGDCuoi, LocalDateTime ngayGDDau);
    List<LichSuGiaoDichResponse> getByTrangThai(String trangThai);
    int deleteOlderThanThreeYears();
}


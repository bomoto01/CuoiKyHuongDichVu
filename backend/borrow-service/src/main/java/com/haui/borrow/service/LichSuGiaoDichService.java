package com.haui.borrow.service;

import com.haui.borrow.dto.response.LichSuGiaoDichResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface LichSuGiaoDichService {
    List<LichSuGiaoDichResponse> getAll();
    List<LichSuGiaoDichResponse> getByUsername(String username);
    List<LichSuGiaoDichResponse> getByMaSach(String maSach);
    List<LichSuGiaoDichResponse> getByNgayGD(LocalDateTime ngayGDDau, LocalDateTime ngayGDCuoi);
    List<LichSuGiaoDichResponse> getByTrangThai(String trangThai);
    int deleteOlderThanThreeYears();
}

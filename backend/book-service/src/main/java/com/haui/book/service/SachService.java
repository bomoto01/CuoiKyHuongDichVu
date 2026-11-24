package com.haui.book.service;

import com.haui.book.dto.request.SachRequest;
import com.haui.book.dto.response.SachResponse;

import java.util.List;

public interface SachService {
    List<SachResponse> getAll();
    SachResponse getById(String maSach);
    List<SachResponse> getByTheLoai(String maTheLoai);
    List<SachResponse> searchByName(String keyword);
    SachResponse add(SachRequest request);
    SachResponse update(String maSach, SachRequest request);
    void delete(String maSach);
    SachResponse huySach(String maSach, Integer soLuongHuy, String username);
    SachResponse adjustStock(String maSach, Integer delta);
}

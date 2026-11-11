package com.haui.library.service;

import com.haui.library.dto.request.SachRequest;
import com.haui.library.dto.response.SachResponse;

import java.util.List;

public interface SachService {
    List<SachResponse> getAll();
    SachResponse getById(String maSach);
    List<SachResponse> getByTheLoai(String maTheLoai);
    List<SachResponse> searchByName(String keyword);
    SachResponse add(SachRequest request);
    SachResponse update(SachRequest request);
    SachResponse addMore(String maSach, Integer soLuongThem);
    SachResponse removeSome(String maSach, Integer soLuongXoa, String username);
    void delete(String maSach);
}


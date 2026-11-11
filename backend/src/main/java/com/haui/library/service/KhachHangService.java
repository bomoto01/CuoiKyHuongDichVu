package com.haui.library.service;

import com.haui.library.dto.request.KhachHangRequest;
import com.haui.library.dto.response.KhachHangResponse;

import java.util.List;

public interface KhachHangService {
    List<KhachHangResponse> getAll();
    KhachHangResponse getById(String maKH);
    List<KhachHangResponse> searchByName(String keyword);
    List<KhachHangResponse> searchBySDT(String sdt);
    KhachHangResponse add(KhachHangRequest request);
    KhachHangResponse update(KhachHangRequest request);
    void delete(String maKH);
}


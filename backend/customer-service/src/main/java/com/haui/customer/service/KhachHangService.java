package com.haui.customer.service;

import com.haui.customer.dto.request.KhachHangRequest;
import com.haui.customer.dto.response.KhachHangResponse;

import java.util.List;

public interface KhachHangService {
    List<KhachHangResponse> getAll();
    KhachHangResponse getById(String maKH);
    List<KhachHangResponse> searchByName(String keyword);
    List<KhachHangResponse> searchBySDT(String sdt);
    KhachHangResponse add(KhachHangRequest request);
    KhachHangResponse update(String maKH, KhachHangRequest request);
    void delete(String maKH);
}

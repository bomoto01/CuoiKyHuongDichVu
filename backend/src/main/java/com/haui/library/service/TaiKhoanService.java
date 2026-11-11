package com.haui.library.service;

import com.haui.library.dto.request.TaiKhoanRequest;
import com.haui.library.dto.response.TaiKhoanResponse;

import java.util.List;

public interface TaiKhoanService {
    List<TaiKhoanResponse> getAll();
    TaiKhoanResponse getByUsername(String username);
    List<TaiKhoanResponse> searchByNameOrUsername(String keyword);
    TaiKhoanResponse add(TaiKhoanRequest request);
    TaiKhoanResponse update(TaiKhoanRequest request);
    void delete(String username);
}


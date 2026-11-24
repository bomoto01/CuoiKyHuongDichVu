package com.haui.account.service;

import com.haui.account.dto.request.TaiKhoanRequest;
import com.haui.account.dto.response.TaiKhoanResponse;

import java.util.List;

public interface TaiKhoanService {
    List<TaiKhoanResponse> getAll();
    TaiKhoanResponse getByUsername(String username);
    List<TaiKhoanResponse> searchByNameOrUsername(String keyword);
    TaiKhoanResponse add(TaiKhoanRequest request);
    TaiKhoanResponse update(String username, TaiKhoanRequest request);
    void delete(String username);
}

package com.haui.library.service;

import com.haui.library.dto.request.TheLoaiRequest;
import com.haui.library.dto.response.TheLoaiResponse;

import java.util.List;

public interface TheLoaiService {
    List<TheLoaiResponse> getAll();
    TheLoaiResponse getById(String maTheLoai);
    List<TheLoaiResponse> searchByName(String keyword);
    TheLoaiResponse add(TheLoaiRequest request);
    TheLoaiResponse update(TheLoaiRequest request);
    void delete(String maTheLoai);
}

package com.haui.book.service;

import com.haui.book.dto.request.TheLoaiRequest;
import com.haui.book.dto.response.TheLoaiResponse;

import java.util.List;

public interface TheLoaiService {
    List<TheLoaiResponse> getAll();
    TheLoaiResponse getById(String maTheLoai);
    List<TheLoaiResponse> searchByName(String keyword);
    TheLoaiResponse add(TheLoaiRequest request);
    TheLoaiResponse update(String maTheLoai, TheLoaiRequest request);
    void delete(String maTheLoai);
}

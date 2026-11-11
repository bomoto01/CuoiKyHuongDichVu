package com.haui.library.controller;

import com.haui.library.dto.request.SachRequest;
import com.haui.library.dto.response.ApiResponse;
import com.haui.library.dto.response.SachResponse;
import com.haui.library.service.SachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Sach")
@RequiredArgsConstructor
@Tag(name = "3.2 Quản lý Sách")
public class SachController {

    private final SachService sachService;

    @Operation(summary = "Lấy danh sách các đầu sách")
    @GetMapping
    public ApiResponse<List<SachResponse>> getAllSach() {
        return ApiResponse.success(sachService.getAll());
    }

    @Operation(summary = "Lấy đầu sách theo mã sách")
    @GetMapping("/GetSachById")
    public ApiResponse<SachResponse> getSachById(@RequestParam("MaSach") String maSach) {
        return ApiResponse.success(sachService.getById(maSach));
    }

    @Operation(summary = "Lấy danh sách đầu sách theo thể loại")
    @GetMapping("/GetSachByTheLoai")
    public ApiResponse<List<SachResponse>> getSachByTheLoai(@RequestParam("MaTheLoai") String maTheLoai) {
        return ApiResponse.success(sachService.getByTheLoai(maTheLoai));
    }

    @Operation(summary = "Lấy danh sách đầu sách có tên chứa keyword")
    @GetMapping("/SearchSachByName")
    public ApiResponse<List<SachResponse>> searchSachByName(@RequestParam("keyword") String keyword) {
        return ApiResponse.success(sachService.searchByName(keyword));
    }

    @Operation(summary = "Thêm một đầu sách mới")
    @PostMapping("/AddSach")
    public ApiResponse<SachResponse> addSach(
            @RequestBody(required = false) @Valid SachRequest bodyRequest,
            @RequestParam(value = "MaSach", required = false) String maSach,
            @RequestParam(value = "TenSach", required = false) String tenSach,
            @RequestParam(value = "SoLuong", required = false) Integer soLuong,
            @RequestParam(value = "TacGia", required = false) String tacGia,
            @RequestParam(value = "MaTheLoai", required = false) String maTheLoai
    ) {
        SachRequest request = bodyRequest != null ? bodyRequest :
                SachRequest.builder()
                        .maSach(maSach)
                        .tenSach(tenSach)
                        .soLuong(soLuong)
                        .tacGia(tacGia)
                        .maTheLoai(maTheLoai)
                        .build();
        return ApiResponse.success("Thêm sách mới thành công", sachService.add(request));
    }

    @Operation(summary = "Sửa thông tin của một đầu sách")
    @PutMapping("/UpdateSach")
    public ApiResponse<SachResponse> updateSach(
            @RequestBody(required = false) @Valid SachRequest bodyRequest,
            @RequestParam(value = "MaSach", required = false) String maSach,
            @RequestParam(value = "TenSach", required = false) String tenSach,
            @RequestParam(value = "SoLuong", required = false) Integer soLuong,
            @RequestParam(value = "TacGia", required = false) String tacGia,
            @RequestParam(value = "MaTheLoai", required = false) String maTheLoai
    ) {
        SachRequest request = bodyRequest != null ? bodyRequest :
                SachRequest.builder()
                        .maSach(maSach)
                        .tenSach(tenSach)
                        .soLuong(soLuong)
                        .tacGia(tacGia)
                        .maTheLoai(maTheLoai)
                        .build();
        return ApiResponse.success("Cập nhật sách thành công", sachService.update(request));
    }

    @Operation(summary = "Thêm một số lượng sách đã tồn tại")
    @PutMapping("/AddMoreSach")
    public ApiResponse<SachResponse> addMoreSach(
            @RequestParam("MaSach") String maSach,
            @RequestParam("SoLuongThem") Integer soLuongThem
    ) {
        return ApiResponse.success("Bổ sung số lượng sách thành công", sachService.addMore(maSach, soLuongThem));
    }

    @Operation(summary = "Hủy bớt một số lượng sách")
    @PutMapping("/RemoveSomeSach")
    public ApiResponse<SachResponse> removeSomeSach(
            @RequestParam("MaSach") String maSach,
            @RequestParam("SoLuongXoa") Integer soLuongXoa,
            @RequestParam(value = "username", required = false) String username
    ) {
        return ApiResponse.success("Hủy số lượng sách thành công", sachService.removeSome(maSach, soLuongXoa, username));
    }

    @Operation(summary = "Xóa hoàn toàn một đầu sách")
    @DeleteMapping("/DeleteSach")
    public ApiResponse<String> deleteSach(@RequestParam("MaSach") String maSach) {
        sachService.delete(maSach);
        return ApiResponse.success("Xóa sách thành công", maSach);
    }
}


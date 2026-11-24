package com.haui.book.controller;

import com.haui.book.dto.request.SachRequest;
import com.haui.book.dto.response.SachResponse;
import com.haui.book.service.SachService;
import com.haui.common.dto.response.ApiResponse;
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
        return ApiResponse.success("Thêm sách thành công", sachService.add(request));
    }

    @Operation(summary = "Sửa thông tin của một đầu sách")
    @PutMapping("/UpdateSach")
    public ApiResponse<SachResponse> updateSach(
            @RequestParam(value = "MaSach", required = false) String queryMaSach,
            @RequestBody(required = false) @Valid SachRequest bodyRequest,
            @RequestParam(value = "TenSach", required = false) String tenSach,
            @RequestParam(value = "SoLuong", required = false) Integer soLuong,
            @RequestParam(value = "TacGia", required = false) String tacGia,
            @RequestParam(value = "MaTheLoai", required = false) String maTheLoai
    ) {
        String targetMaSach = (bodyRequest != null && bodyRequest.getMaSach() != null) ? bodyRequest.getMaSach() : queryMaSach;
        SachRequest request = bodyRequest != null ? bodyRequest :
                SachRequest.builder()
                        .maSach(targetMaSach)
                        .tenSach(tenSach)
                        .soLuong(soLuong)
                        .tacGia(tacGia)
                        .maTheLoai(maTheLoai)
                        .build();
        return ApiResponse.success("Cập nhật thông tin sách thành công", sachService.update(targetMaSach, request));
    }

    @Operation(summary = "Xóa một đầu sách")
    @DeleteMapping("/DeleteSach")
    public ApiResponse<String> deleteSach(@RequestParam("MaSach") String maSach) {
        sachService.delete(maSach);
        return ApiResponse.success("Xóa sách thành công", maSach);
    }

    @Operation(summary = "Hủy một lượng sách khỏi kho")
    @PostMapping("/HuySach")
    public ApiResponse<SachResponse> huySach(
            @RequestParam("MaSach") String maSach,
            @RequestParam("SoLuongHuy") Integer soLuongHuy,
            @RequestParam(value = "Username", required = false) String username
    ) {
        return ApiResponse.success("Hủy sách thành công", sachService.huySach(maSach, soLuongHuy, username));
    }

    @Operation(summary = "Điều chỉnh tồn kho nội bộ cho Borrow Service")
    @PostMapping("/adjust-stock")
    public ApiResponse<SachResponse> adjustStock(
            @RequestParam("maSach") String maSach,
            @RequestParam("delta") Integer delta
    ) {
        return ApiResponse.success("Điều chỉnh tồn kho thành công", sachService.adjustStock(maSach, delta));
    }
}

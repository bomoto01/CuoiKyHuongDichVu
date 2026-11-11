package com.haui.library.controller;

import com.haui.library.dto.request.KhachHangRequest;
import com.haui.library.dto.response.ApiResponse;
import com.haui.library.dto.response.KhachHangResponse;
import com.haui.library.service.KhachHangService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/KhachHang")
@RequiredArgsConstructor
@Tag(name = "3.1 Quản lý Khách Hàng (Bạn đọc)")
public class KhachHangController {

    private final KhachHangService khachHangService;

    @Operation(summary = "Lấy danh sách tất cả khách hàng")
    @GetMapping
    public ApiResponse<List<KhachHangResponse>> getAllKhachHang() {
        return ApiResponse.success(khachHangService.getAll());
    }

    @Operation(summary = "Lấy khách hàng theo mã")
    @GetMapping("/GetKhachHangById")
    public ApiResponse<KhachHangResponse> getKhachHangById(@RequestParam("MaKH") String maKH) {
        return ApiResponse.success(khachHangService.getById(maKH));
    }

    @Operation(summary = "Tìm kiếm khách hàng theo tên")
    @GetMapping("/SearchKhachHangByName")
    public ApiResponse<List<KhachHangResponse>> searchKhachHangByName(@RequestParam("keyword") String keyword) {
        return ApiResponse.success(khachHangService.searchByName(keyword));
    }

    @Operation(summary = "Tìm kiếm khách hàng theo số điện thoại")
    @GetMapping("/SearchKhachHangBySDT")
    public ApiResponse<List<KhachHangResponse>> searchKhachHangBySDT(@RequestParam("sdt") String sdt) {
        return ApiResponse.success(khachHangService.searchBySDT(sdt));
    }

    @Operation(summary = "Thêm mới một khách hàng")
    @PostMapping("/AddKhachHang")
    public ApiResponse<KhachHangResponse> addKhachHang(
            @RequestBody(required = false) @Valid KhachHangRequest bodyRequest,
            @RequestParam(value = "MaKH", required = false) String maKH,
            @RequestParam(value = "TenKH", required = false) String tenKH,
            @RequestParam(value = "SdtKH", required = false) String sdtKH
    ) {
        KhachHangRequest request = bodyRequest != null ? bodyRequest :
                KhachHangRequest.builder().maKH(maKH).tenKH(tenKH).sdtKH(sdtKH).build();
        return ApiResponse.success("Thêm khách hàng thành công", khachHangService.add(request));
    }

    @Operation(summary = "Sửa thông tin khách hàng")
    @PutMapping("/UpdateKhachHang")
    public ApiResponse<KhachHangResponse> updateKhachHang(
            @RequestBody(required = false) @Valid KhachHangRequest bodyRequest,
            @RequestParam(value = "MaKH", required = false) String maKH,
            @RequestParam(value = "TenKH", required = false) String tenKH,
            @RequestParam(value = "SdtKH", required = false) String sdtKH
    ) {
        KhachHangRequest request = bodyRequest != null ? bodyRequest :
                KhachHangRequest.builder().maKH(maKH).tenKH(tenKH).sdtKH(sdtKH).build();
        return ApiResponse.success("Cập nhật thông tin khách hàng thành công", khachHangService.update(request));
    }

    @Operation(summary = "Xóa một khách hàng")
    @DeleteMapping("/DeleteKhachHang")
    public ApiResponse<String> deleteKhachHang(@RequestParam("MaKH") String maKH) {
        khachHangService.delete(maKH);
        return ApiResponse.success("Xóa khách hàng thành công", maKH);
    }
}

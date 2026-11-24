package com.haui.borrow.controller;

import com.haui.borrow.dto.response.LichSuGiaoDichResponse;
import com.haui.borrow.service.LichSuGiaoDichService;
import com.haui.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping({"/api/LiSuGiaoDich", "/api/LichSuGiaoDich"})
@RequiredArgsConstructor
@Tag(name = "3.6 Quản lý Lịch Sử Giao Dịch")
public class LiSuGiaoDichController {

    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private final LichSuGiaoDichService lichSuGiaoDichService;

    @Operation(summary = "Lấy danh sách tất cả giao dịch đã hoàn thành")
    @GetMapping
    public ApiResponse<List<LichSuGiaoDichResponse>> getAllLSuGiaoDich() {
        return ApiResponse.success(lichSuGiaoDichService.getAll());
    }

    @Operation(summary = "Lấy danh sách giao dịch theo Username của thủ thư thực hiện")
    @GetMapping("/GetLSuGiaoDichByUsername")
    public ApiResponse<List<LichSuGiaoDichResponse>> getLSuGiaoDichByUsername(@RequestParam("Username") String username) {
        return ApiResponse.success(lichSuGiaoDichService.getByUsername(username));
    }

    @Operation(summary = "Lấy danh sách giao dịch theo mã sách")
    @GetMapping("/GetLSuGiaoDichByMaSach")
    public ApiResponse<List<LichSuGiaoDichResponse>> getLSuGiaoDichByMaSach(@RequestParam("MaSach") String maSach) {
        return ApiResponse.success(lichSuGiaoDichService.getByMaSach(maSach));
    }

    @Operation(summary = "Lấy danh sách giao dịch theo khoảng ngày giao dịch")
    @GetMapping("/GetLSuGiaoDichByNgayGD")
    public ApiResponse<List<LichSuGiaoDichResponse>> getLSuGiaoDichByNgayGD(
            @RequestParam(value = "NgayGDDau", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayGDDau,
            @RequestParam(value = "NgayGDCuoi", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayGDCuoi
    ) {
        LocalDateTime start = ngayGDDau != null ? ngayGDDau.atStartOfDay() : LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime end = ngayGDCuoi != null ? ngayGDCuoi.atTime(LocalTime.MAX) : LocalDateTime.now(VIETNAM_ZONE);
        return ApiResponse.success(lichSuGiaoDichService.getByNgayGD(start, end));
    }

    @Operation(summary = "Lấy danh sách giao dịch theo trạng thái")
    @GetMapping("/GetLSuGiaoDichByTrangThai")
    public ApiResponse<List<LichSuGiaoDichResponse>> getLSuGiaoDichByTrangThai(@RequestParam("TrangThai") String trangThai) {
        return ApiResponse.success(lichSuGiaoDichService.getByTrangThai(trangThai));
    }

    @Operation(summary = "Xóa các giao dịch đã qua 3 năm")
    @DeleteMapping("/DeleteLSuGiaoDichQua3Nam")
    public ApiResponse<String> deleteLSuGiaoDichQua3Nam() {
        int deleted = lichSuGiaoDichService.deleteOlderThanThreeYears();
        return ApiResponse.success("Đã xóa " + deleted + " giao dịch quá hạn 3 năm", "Success");
    }
}

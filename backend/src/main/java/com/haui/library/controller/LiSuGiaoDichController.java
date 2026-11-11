package com.haui.library.controller;

import com.haui.library.dto.response.ApiResponse;
import com.haui.library.dto.response.LichSuGiaoDichResponse;
import com.haui.library.service.LichSuGiaoDichService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping({"/api/LiSuGiaoDich", "/api/LichSuGiaoDich"})
@RequiredArgsConstructor
@Tag(name = "3.6 Quản lý Lịch Sử Giao Dịch")
public class LiSuGiaoDichController {

    private final LichSuGiaoDichService lichSuGiaoDichService;

    @Operation(summary = "Lấy danh sách tất cả giao dịch đã hoàn thành")
    @GetMapping
    public ApiResponse<List<LichSuGiaoDichResponse>> getAllLichSuGiaoDich() {
        return ApiResponse.success(lichSuGiaoDichService.getAll());
    }

    @Operation(summary = "Lấy danh sách giao dịch theo Username")
    @GetMapping("/GetLSuGiaoDichByUsername")
    public ApiResponse<List<LichSuGiaoDichResponse>> getLSuGiaoDichByUsername(
            @RequestParam(value = "Username", required = false) String usernameUpper,
            @RequestParam(value = "username", required = false) String usernameLower
    ) {
        String username = usernameUpper != null ? usernameUpper : usernameLower;
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
        LocalDateTime end = ngayGDCuoi != null ? ngayGDCuoi.atTime(LocalTime.MAX) : LocalDateTime.now();
        return ApiResponse.success(lichSuGiaoDichService.getByNgayGD(start, end));
    }

    @Operation(summary = "Lấy danh sách giao dịch theo trạng thái")
    @GetMapping("/GetLSuGiaoDichByTrangThai")
    public ApiResponse<List<LichSuGiaoDichResponse>> getLSuGiaoDichByTrangThai(@RequestParam("TrangThai") String trangThai) {
        return ApiResponse.success(lichSuGiaoDichService.getByTrangThai(trangThai));
    }

    @Operation(summary = "Xóa các giao dịch đã qua 3 năm")
    @DeleteMapping
    public ApiResponse<String> deleteLiSuGiaoDichByYear() {
        int count = lichSuGiaoDichService.deleteOlderThanThreeYears();
        return ApiResponse.success("Đã xóa các giao dịch cũ hơn 3 năm", "Đã xóa " + count + " bản ghi");
    }

    @Operation(summary = "Xóa các giao dịch đã qua 3 năm (alias endpoint)")
    @DeleteMapping("/DeleteLiSuGiaoDichByYear")
    public ApiResponse<String> deleteLiSuGiaoDichByYearAlias() {
        int count = lichSuGiaoDichService.deleteOlderThanThreeYears();
        return ApiResponse.success("Đã xóa các giao dịch cũ hơn 3 năm", "Đã xóa " + count + " bản ghi");
    }
}

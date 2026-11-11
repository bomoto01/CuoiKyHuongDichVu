package com.haui.library.controller;

import com.haui.library.dto.request.TheLoaiRequest;
import com.haui.library.dto.response.ApiResponse;
import com.haui.library.dto.response.TheLoaiResponse;
import com.haui.library.service.TheLoaiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/TheLoai")
@RequiredArgsConstructor
@Tag(name = "3.4 Quản lý Thể Loại")
public class TheLoaiController {

    private final TheLoaiService theLoaiService;

    @Operation(summary = "Lấy danh sách thể loại hoặc lọc theo mã/tên")
    @GetMapping
    public ApiResponse<?> getTheLoai(
            @RequestParam(value = "MaTheLoai", required = false) String maTheLoai,
            @RequestParam(value = "TenTheLoai", required = false) String tenTheLoai
    ) {
        if (maTheLoai != null && !maTheLoai.isBlank()) {
            return ApiResponse.success(theLoaiService.getById(maTheLoai));
        }
        if (tenTheLoai != null && !tenTheLoai.isBlank()) {
            return ApiResponse.success(theLoaiService.searchByName(tenTheLoai));
        }
        return ApiResponse.success(theLoaiService.getAll());
    }

    @Operation(summary = "Lấy thể loại theo mã thể loại")
    @GetMapping("/GetTheLoaiById")
    public ApiResponse<TheLoaiResponse> getTheLoaiById(@RequestParam("MaTheLoai") String maTheLoai) {
        return ApiResponse.success(theLoaiService.getById(maTheLoai));
    }

    @Operation(summary = "Tìm kiếm thể loại theo tên")
    @GetMapping("/SearchTheLoaiByName")
    public ApiResponse<List<TheLoaiResponse>> searchTheLoaiByName(@RequestParam("keyword") String keyword) {
        return ApiResponse.success(theLoaiService.searchByName(keyword));
    }

    @Operation(summary = "Thêm một thể loại mới")
    @PostMapping("/AddTheLoai")
    public ApiResponse<TheLoaiResponse> addTheLoai(
            @RequestBody(required = false) @Valid TheLoaiRequest bodyRequest,
            @RequestParam(value = "MaTheLoai", required = false) String maTheLoai,
            @RequestParam(value = "TenTheLoai", required = false) String tenTheLoai
    ) {
        TheLoaiRequest request = bodyRequest != null ? bodyRequest :
                TheLoaiRequest.builder().maTheLoai(maTheLoai).tenTheLoai(tenTheLoai).build();
        return ApiResponse.success("Thêm thể loại thành công", theLoaiService.add(request));
    }

    @Operation(summary = "Sửa thông tin của một thể loại")
    @PutMapping("/UpdateTheLoai")
    public ApiResponse<TheLoaiResponse> updateTheLoai(
            @RequestBody(required = false) @Valid TheLoaiRequest bodyRequest,
            @RequestParam(value = "MaTheLoai", required = false) String maTheLoai,
            @RequestParam(value = "TenTheLoai", required = false) String tenTheLoai
    ) {
        TheLoaiRequest request = bodyRequest != null ? bodyRequest :
                TheLoaiRequest.builder().maTheLoai(maTheLoai).tenTheLoai(tenTheLoai).build();
        return ApiResponse.success("Cập nhật thể loại thành công", theLoaiService.update(request));
    }

    @Operation(summary = "Xóa một thể loại đang có")
    @DeleteMapping("/DeleteTheLoai")
    public ApiResponse<String> deleteTheLoai(@RequestParam("MaTheLoai") String maTheLoai) {
        theLoaiService.delete(maTheLoai);
        return ApiResponse.success("Xóa thể loại thành công", maTheLoai);
    }
}

package com.haui.library.controller;

import com.haui.library.dto.request.AuthenticationRequest;
import com.haui.library.dto.request.TaiKhoanRequest;
import com.haui.library.dto.response.ApiResponse;
import com.haui.library.dto.response.AuthenticationResponse;
import com.haui.library.dto.response.TaiKhoanResponse;
import com.haui.library.enums.Role;
import com.haui.library.service.AuthenticationService;
import com.haui.library.service.TaiKhoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/TaiKhoan")
@RequiredArgsConstructor
@Tag(name = "3.5 Quản lý Tài Khoản")
public class TaiKhoanController {

    private final TaiKhoanService taiKhoanService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Lấy danh sách tất cả tài khoản")
    @GetMapping
    public ApiResponse<List<TaiKhoanResponse>> getAllTaiKhoan() {
        return ApiResponse.success(taiKhoanService.getAll());
    }

    @Operation(summary = "Lấy thông tin tài khoản theo username")
    @GetMapping("/GetTaiKhoanByUsername")
    public ApiResponse<TaiKhoanResponse> getTaiKhoanByUsername(@RequestParam("username") String username) {
        return ApiResponse.success(taiKhoanService.getByUsername(username));
    }

    @Operation(summary = "Tìm kiếm tài khoản theo tên hoặc username")
    @GetMapping("/SearchTaiKhoan")
    public ApiResponse<List<TaiKhoanResponse>> searchTaiKhoan(@RequestParam("keyword") String keyword) {
        return ApiResponse.success(taiKhoanService.searchByNameOrUsername(keyword));
    }

    @Operation(summary = "Lấy thông tin tài khoản hiện tại đang đăng nhập")
    @GetMapping("/my-info")
    public ApiResponse<TaiKhoanResponse> getMyInfo() {
        return ApiResponse.success(authenticationService.getMyInfo());
    }

    @Operation(summary = "Đăng nhập vào hệ thống (POST)")
    @PostMapping("/Login")
    public ApiResponse<AuthenticationResponse> loginPost(
            @RequestBody(required = false) AuthenticationRequest bodyRequest,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password
    ) {
        String finalUsername = (bodyRequest != null && bodyRequest.getUsername() != null) ? bodyRequest.getUsername() : username;
        String finalPassword = (bodyRequest != null && bodyRequest.getPassword() != null) ? bodyRequest.getPassword() : password;
        AuthenticationRequest request = new AuthenticationRequest(finalUsername, finalPassword);
        return ApiResponse.success("Đăng nhập thành công", authenticationService.authenticate(request));
    }

    @Operation(summary = "Đăng nhập vào hệ thống (GET - theo đặc tả bảng 3.5)")
    @GetMapping("/Login")
    public ApiResponse<AuthenticationResponse> loginGet(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        return ApiResponse.success("Đăng nhập thành công", authenticationService.authenticate(request));
    }

    @Operation(summary = "Thêm tài khoản mới")
    @PostMapping("/AddTaiKhoan")
    public ApiResponse<TaiKhoanResponse> addTaiKhoan(
            @RequestBody(required = false) @Valid TaiKhoanRequest bodyRequest,
            @RequestParam(value = "Username", required = false) String username,
            @RequestParam(value = "Pass", required = false) String pass,
            @RequestParam(value = "TenTK", required = false) String tenTK,
            @RequestParam(value = "SdtTK", required = false) String sdtTK,
            @RequestParam(value = "Role", required = false) String roleStr
    ) {
        Role role = null;
        if (roleStr != null && !roleStr.isBlank()) {
            role = Role.valueOf(roleStr.toUpperCase());
        }
        TaiKhoanRequest request = bodyRequest != null ? bodyRequest :
                TaiKhoanRequest.builder()
                        .username(username)
                        .pass(pass)
                        .tenTK(tenTK)
                        .sdtTK(sdtTK)
                        .role(role)
                        .build();
        return ApiResponse.success("Thêm tài khoản mới thành công", taiKhoanService.add(request));
    }

    @Operation(summary = "Sửa thông tin của một tài khoản")
    @PutMapping("/UpdateTaiKhoan")
    public ApiResponse<TaiKhoanResponse> updateTaiKhoan(
            @RequestBody(required = false) @Valid TaiKhoanRequest bodyRequest,
            @RequestParam(value = "Username", required = false) String username,
            @RequestParam(value = "Pass", required = false) String pass,
            @RequestParam(value = "TenTK", required = false) String tenTK,
            @RequestParam(value = "SdtTK", required = false) String sdtTK,
            @RequestParam(value = "Role", required = false) String roleStr
    ) {
        Role role = null;
        if (roleStr != null && !roleStr.isBlank()) {
            role = Role.valueOf(roleStr.toUpperCase());
        }
        TaiKhoanRequest request = bodyRequest != null ? bodyRequest :
                TaiKhoanRequest.builder()
                        .username(username)
                        .pass(pass)
                        .tenTK(tenTK)
                        .sdtTK(sdtTK)
                        .role(role)
                        .build();
        return ApiResponse.success("Cập nhật thông tin tài khoản thành công", taiKhoanService.update(request));
    }

    @Operation(summary = "Xóa một tài khoản")
    @DeleteMapping("/DeleteTaiKhoan")
    public ApiResponse<String> deleteTaiKhoan(
            @RequestParam(value = "Username", required = false) String usernameUpper,
            @RequestParam(value = "username", required = false) String usernameLower
    ) {
        String username = usernameUpper != null ? usernameUpper : usernameLower;
        taiKhoanService.delete(username);
        return ApiResponse.success("Xóa tài khoản thành công", username);
    }
}


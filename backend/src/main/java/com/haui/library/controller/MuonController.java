package com.haui.library.controller;

import com.haui.library.dto.request.MatSachRequest;
import com.haui.library.dto.request.PhieuMuonCreationRequest;
import com.haui.library.dto.request.TraSachRequest;
import com.haui.library.dto.response.ApiResponse;
import com.haui.library.dto.response.PhieuMuonResponse;
import com.haui.library.service.PhieuMuonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/Muon")
@RequiredArgsConstructor
@Tag(name = "3.3 Quản lý Đơn Mượn")
public class MuonController {

    private final PhieuMuonService phieuMuonService;

    @Operation(summary = "Lấy danh sách tất cả đơn mượn sách")
    @GetMapping
    public ApiResponse<List<PhieuMuonResponse>> getAllMuon() {
        return ApiResponse.success(phieuMuonService.getAll());
    }

    @Operation(summary = "Lấy danh sách đơn mượn theo mã khách hàng")
    @GetMapping("/GetMuonByMaKH")
    public ApiResponse<List<PhieuMuonResponse>> getMuonByMaKH(@RequestParam("MaKH") String maKH) {
        return ApiResponse.success(phieuMuonService.getByMaKH(maKH));
    }

    @Operation(summary = "Lấy đơn mượn theo mã mượn")
    @GetMapping("/GetMuonByMaMuon")
    public ApiResponse<PhieuMuonResponse> getMuonByMaMuon(@RequestParam("MaMuon") String maMuon) {
        return ApiResponse.success(phieuMuonService.getByMaMuon(maMuon));
    }

    @Operation(summary = "Lấy danh sách đơn mượn theo mã sách")
    @GetMapping("/GetMuonByMaSach")
    public ApiResponse<List<PhieuMuonResponse>> getMuonByMaSach(@RequestParam("MaSach") String maSach) {
        return ApiResponse.success(phieuMuonService.getByMaSach(maSach));
    }

    @Operation(summary = "Lấy danh sách đơn mượn theo ngày mượn")
    @GetMapping("/GetMuonByNgayMuon")
    public ApiResponse<List<PhieuMuonResponse>> getMuonByNgayMuon(
            @RequestParam("NgayMuon") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayMuon
    ) {
        return ApiResponse.success(phieuMuonService.getByNgayMuon(ngayMuon));
    }

    @Operation(summary = "Lấy danh sách các đơn mượn quá hạn trả")
    @GetMapping("/GetOverdue")
    public ApiResponse<List<PhieuMuonResponse>> getOverdue() {
        return ApiResponse.success(phieuMuonService.getOverdue());
    }

    @Operation(summary = "Tạo đơn mượn sách mới")
    @PostMapping("/AddMuon")
    public ApiResponse<PhieuMuonResponse> addMuon(
            @RequestBody(required = false) @Valid PhieuMuonCreationRequest bodyRequest,
            @RequestParam(value = "MaMuon", required = false) String maMuon,
            @RequestParam(value = "MaKH", required = false) String maKH,
            @RequestParam(value = "MaSach", required = false) String maSach,
            @RequestParam(value = "SoLuong", required = false) Integer soLuong,
            @RequestParam(value = "NgayMuon", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayMuon,
            @RequestParam(value = "NgayTra", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayTra,
            @RequestParam(value = "username", required = false) String username
    ) {
        PhieuMuonCreationRequest request = bodyRequest != null ? bodyRequest :
                PhieuMuonCreationRequest.builder()
                        .maMuon(maMuon)
                        .maKH(maKH)
                        .maSach(maSach)
                        .soLuong(soLuong)
                        .ngayMuon(ngayMuon)
                        .ngayTra(ngayTra)
                        .username(username)
                        .build();
        return ApiResponse.success("Tạo đơn mượn sách thành công", phieuMuonService.addMuon(request));
    }

    @Operation(summary = "Trả sách")
    @PutMapping("/TraSach")
    public ApiResponse<PhieuMuonResponse> traSach(
            @RequestBody(required = false) @Valid TraSachRequest bodyRequest,
            @RequestParam(value = "MaMuon", required = false) String maMuon,
            @RequestParam(value = "Username", required = false) String username,
            @RequestParam(value = "username", required = false) String usernameLower,
            @RequestParam(value = "SoLuong", required = false) Integer soLuong
    ) {
        String finalUsername = username != null ? username : usernameLower;
        TraSachRequest request = bodyRequest != null ? bodyRequest :
                TraSachRequest.builder()
                        .maMuon(maMuon)
                        .username(finalUsername)
                        .soLuong(soLuong)
                        .build();
        return ApiResponse.success("Trả sách thành công", phieuMuonService.traSach(request));
    }

    @Operation(summary = "Báo mất sách")
    @PutMapping("/MatSach")
    public ApiResponse<PhieuMuonResponse> matSach(
            @RequestBody(required = false) @Valid MatSachRequest bodyRequest,
            @RequestParam(value = "MaMuon", required = false) String maMuon,
            @RequestParam(value = "Username", required = false) String username,
            @RequestParam(value = "username", required = false) String usernameLower,
            @RequestParam(value = "SoLuongMat", required = false) Integer soLuongMat
    ) {
        String finalUsername = username != null ? username : usernameLower;
        MatSachRequest request = bodyRequest != null ? bodyRequest :
                MatSachRequest.builder()
                        .maMuon(maMuon)
                        .username(finalUsername)
                        .soLuongMat(soLuongMat)
                        .build();
        return ApiResponse.success("Ghi nhận mất sách thành công", phieuMuonService.matSach(request));
    }
}

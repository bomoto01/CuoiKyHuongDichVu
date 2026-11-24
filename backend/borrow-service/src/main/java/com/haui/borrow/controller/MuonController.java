package com.haui.borrow.controller;

import com.haui.borrow.dto.request.MatSachRequest;
import com.haui.borrow.dto.request.PhieuMuonCreationRequest;
import com.haui.borrow.dto.request.TraSachRequest;
import com.haui.borrow.dto.response.PhieuMuonResponse;
import com.haui.borrow.service.PhieuMuonService;
import com.haui.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping({"/api/Muon", "/api/PhieuMuon"})
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

    @Operation(summary = "Tạo một phiếu mượn mới")
    @PostMapping("/AddMuon")
    public ApiResponse<PhieuMuonResponse> addMuon(
            @RequestBody(required = false) @Valid PhieuMuonCreationRequest bodyRequest,
            @RequestParam(value = "MaMuon", required = false) String maMuon,
            @RequestParam(value = "MaKH", required = false) String maKH,
            @RequestParam(value = "MaSach", required = false) String maSach,
            @RequestParam(value = "SoLuong", required = false) Integer soLuong,
            @RequestParam(value = "NgayMuon", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayMuon,
            @RequestParam(value = "NgayTra", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayTra,
            @RequestParam(value = "Username", required = false) String username
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
        return ApiResponse.success("Tạo phiếu mượn thành công", phieuMuonService.addMuon(request));
    }

    @Operation(summary = "Thực hiện trả sách")
    @PostMapping("/TraSach")
    public ApiResponse<PhieuMuonResponse> traSach(
            @RequestBody(required = false) @Valid TraSachRequest bodyRequest,
            @RequestParam(value = "MaMuon", required = false) String maMuon,
            @RequestParam(value = "SoLuong", required = false) Integer soLuong,
            @RequestParam(value = "Username", required = false) String username
    ) {
        TraSachRequest request = bodyRequest != null ? bodyRequest :
                TraSachRequest.builder().maMuon(maMuon).soLuong(soLuong).username(username).build();
        return ApiResponse.success("Trả sách thành công", phieuMuonService.traSach(request));
    }

    @Operation(summary = "Báo mất sách")
    @PostMapping("/MatSach")
    public ApiResponse<PhieuMuonResponse> matSach(
            @RequestBody(required = false) @Valid MatSachRequest bodyRequest,
            @RequestParam(value = "MaMuon", required = false) String maMuon,
            @RequestParam(value = "SoLuongMat", required = false) Integer soLuongMat,
            @RequestParam(value = "Username", required = false) String username
    ) {
        MatSachRequest request = bodyRequest != null ? bodyRequest :
                MatSachRequest.builder().maMuon(maMuon).soLuongMat(soLuongMat).username(username).build();
        return ApiResponse.success("Đã ghi nhận báo mất sách", phieuMuonService.matSach(request));
    }
}

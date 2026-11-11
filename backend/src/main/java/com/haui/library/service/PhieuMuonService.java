package com.haui.library.service;

import com.haui.library.dto.request.MatSachRequest;
import com.haui.library.dto.request.PhieuMuonCreationRequest;
import com.haui.library.dto.request.TraSachRequest;
import com.haui.library.dto.response.PhieuMuonResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface PhieuMuonService {
    List<PhieuMuonResponse> getAll();
    List<PhieuMuonResponse> getByMaKH(String maKH);
    PhieuMuonResponse getByMaMuon(String maMuon);
    List<PhieuMuonResponse> getByMaSach(String maSach);
    List<PhieuMuonResponse> getByNgayMuon(LocalDateTime ngayMuon);
    List<PhieuMuonResponse> getOverdue();
    PhieuMuonResponse addMuon(PhieuMuonCreationRequest request);
    PhieuMuonResponse traSach(TraSachRequest request);
    PhieuMuonResponse matSach(MatSachRequest request);
}


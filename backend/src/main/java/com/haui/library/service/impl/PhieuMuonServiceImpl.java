package com.haui.library.service.impl;

import com.haui.library.dto.request.MatSachRequest;
import com.haui.library.dto.request.PhieuMuonCreationRequest;
import com.haui.library.dto.request.TraSachRequest;
import com.haui.library.dto.response.PhieuMuonResponse;
import com.haui.library.entity.*;
import com.haui.library.enums.TransactionStatus;
import com.haui.library.exception.AppException;
import com.haui.library.exception.ErrorCode;
import com.haui.library.mapper.PhieuMuonMapper;
import com.haui.library.repository.*;
import com.haui.library.service.PhieuMuonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhieuMuonServiceImpl implements PhieuMuonService {

    private final PhieuMuonRepository phieuMuonRepository;
    private final KhachHangRepository khachHangRepository;
    private final SachRepository sachRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final LichSuGiaoDichRepository lichSuGiaoDichRepository;
    private final PhieuMuonMapper phieuMuonMapper;

    @Override
    public List<PhieuMuonResponse> getAll() {
        return phieuMuonMapper.toResponseList(phieuMuonRepository.findAll());
    }

    @Override
    public List<PhieuMuonResponse> getByMaKH(String maKH) {
        return phieuMuonMapper.toResponseList(phieuMuonRepository.findByKhachHang_MaKH(maKH));
    }

    @Override
    public PhieuMuonResponse getByMaMuon(String maMuon) {
        PhieuMuon entity = phieuMuonRepository.findById(maMuon)
                .orElseThrow(() -> new AppException(ErrorCode.BORROW_NOT_FOUND));
        return phieuMuonMapper.toResponse(entity);
    }

    @Override
    public List<PhieuMuonResponse> getByMaSach(String maSach) {
        return phieuMuonMapper.toResponseList(phieuMuonRepository.findBySach_MaSach(maSach));
    }

    @Override
    public List<PhieuMuonResponse> getByNgayMuon(LocalDateTime ngayMuon) {
        LocalDate date = ngayMuon.toLocalDate();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return phieuMuonMapper.toResponseList(phieuMuonRepository.findByNgayMuonBetween(startOfDay, endOfDay));
    }

    @Override
    public List<PhieuMuonResponse> getOverdue() {
        return phieuMuonMapper.toResponseList(phieuMuonRepository.findByNgayTraBefore(LocalDateTime.now()));
    }

    @Override
    @Transactional
    public PhieuMuonResponse addMuon(PhieuMuonCreationRequest request) {
        if (phieuMuonRepository.existsById(request.getMaMuon())) {
            throw new AppException(ErrorCode.BORROW_EXISTED);
        }

        KhachHang khachHang = khachHangRepository.findById(request.getMaKH())
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        Sach sach = sachRepository.findById(request.getMaSach())
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        if (sach.getSoLuong() < request.getSoLuong()) {
            throw new AppException(ErrorCode.BOOK_INSUFFICIENT_STOCK);
        }

        LocalDateTime ngayMuon = request.getNgayMuon() != null ? request.getNgayMuon() : LocalDateTime.now();
        LocalDateTime ngayTra = request.getNgayTra() != null ? request.getNgayTra() : ngayMuon.plusDays(14);

        if (ngayTra.isBefore(ngayMuon)) {
            throw new AppException(ErrorCode.INVALID_BORROW_DATE);
        }

        // Deduct book inventory
        sach.setSoLuong(sach.getSoLuong() - request.getSoLuong());
        sachRepository.save(sach);

        // Save borrow slip
        PhieuMuon phieuMuon = PhieuMuon.builder()
                .maMuon(request.getMaMuon())
                .khachHang(khachHang)
                .sach(sach)
                .soLuong(request.getSoLuong())
                .ngayMuon(ngayMuon)
                .ngayTra(ngayTra)
                .build();
        PhieuMuon savedPhieuMuon = phieuMuonRepository.save(phieuMuon);

        // Record to LichSuGiaoDich
        TaiKhoan taiKhoan = null;
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            taiKhoan = taiKhoanRepository.findByUsername(request.getUsername()).orElse(null);
        }

        String maGD = "GD" + System.currentTimeMillis() % 1000000;
        LichSuGiaoDich lsgd = LichSuGiaoDich.builder()
                .maGD(maGD)
                .ngayGD(LocalDateTime.now())
                .soLuong(request.getSoLuong())
                .trangThai(TransactionStatus.MUON.getDescription())
                .sach(sach)
                .khachHang(khachHang)
                .taiKhoan(taiKhoan)
                .build();
        lichSuGiaoDichRepository.save(lsgd);

        return phieuMuonMapper.toResponse(savedPhieuMuon);
    }

    @Override
    @Transactional
    public PhieuMuonResponse traSach(TraSachRequest request) {
        PhieuMuon phieuMuon = phieuMuonRepository.findById(request.getMaMuon())
                .orElseThrow(() -> new AppException(ErrorCode.BORROW_NOT_FOUND));

        if (request.getSoLuong() > phieuMuon.getSoLuong()) {
            throw new AppException(ErrorCode.INVALID_RETURN_QUANTITY);
        }

        Sach sach = phieuMuon.getSach();
        sach.setSoLuong(sach.getSoLuong() + request.getSoLuong());
        sachRepository.save(sach);

        TaiKhoan taiKhoan = null;
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            taiKhoan = taiKhoanRepository.findByUsername(request.getUsername()).orElse(null);
        }

        // Record into transaction history
        String maGD = "GD" + System.currentTimeMillis() % 1000000;
        LichSuGiaoDich lsgd = LichSuGiaoDich.builder()
                .maGD(maGD)
                .ngayGD(LocalDateTime.now())
                .soLuong(request.getSoLuong())
                .trangThai(TransactionStatus.TRA.getDescription())
                .sach(sach)
                .khachHang(phieuMuon.getKhachHang())
                .taiKhoan(taiKhoan)
                .build();
        lichSuGiaoDichRepository.save(lsgd);

        if (request.getSoLuong().equals(phieuMuon.getSoLuong())) {
            phieuMuonRepository.delete(phieuMuon);
            return phieuMuonMapper.toResponse(phieuMuon);
        } else {
            phieuMuon.setSoLuong(phieuMuon.getSoLuong() - request.getSoLuong());
            return phieuMuonMapper.toResponse(phieuMuonRepository.save(phieuMuon));
        }
    }

    @Override
    @Transactional
    public PhieuMuonResponse matSach(MatSachRequest request) {
        PhieuMuon phieuMuon = phieuMuonRepository.findById(request.getMaMuon())
                .orElseThrow(() -> new AppException(ErrorCode.BORROW_NOT_FOUND));

        if (request.getSoLuongMat() > phieuMuon.getSoLuong()) {
            throw new AppException(ErrorCode.INVALID_LOST_QUANTITY);
        }

        Sach sach = phieuMuon.getSach();

        TaiKhoan taiKhoan = null;
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            taiKhoan = taiKhoanRepository.findByUsername(request.getUsername()).orElse(null);
        }

        // Record into transaction history with status MAT
        String maGD = "GD" + System.currentTimeMillis() % 1000000;
        LichSuGiaoDich lsgd = LichSuGiaoDich.builder()
                .maGD(maGD)
                .ngayGD(LocalDateTime.now())
                .soLuong(request.getSoLuongMat())
                .trangThai(TransactionStatus.MAT.getDescription())
                .sach(sach)
                .khachHang(phieuMuon.getKhachHang())
                .taiKhoan(taiKhoan)
                .build();
        lichSuGiaoDichRepository.save(lsgd);

        if (request.getSoLuongMat().equals(phieuMuon.getSoLuong())) {
            phieuMuonRepository.delete(phieuMuon);
            return phieuMuonMapper.toResponse(phieuMuon);
        } else {
            phieuMuon.setSoLuong(phieuMuon.getSoLuong() - request.getSoLuongMat());
            return phieuMuonMapper.toResponse(phieuMuonRepository.save(phieuMuon));
        }
    }
}

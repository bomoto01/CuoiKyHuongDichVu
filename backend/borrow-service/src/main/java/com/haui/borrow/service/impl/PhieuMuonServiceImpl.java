package com.haui.borrow.service.impl;

import com.haui.borrow.client.BookServiceClient;
import com.haui.borrow.client.CustomerServiceClient;
import com.haui.borrow.client.dto.BookDto;
import com.haui.borrow.client.dto.CustomerDto;
import com.haui.borrow.dto.request.MatSachRequest;
import com.haui.borrow.dto.request.PhieuMuonCreationRequest;
import com.haui.borrow.dto.request.TraSachRequest;
import com.haui.borrow.dto.response.PhieuMuonResponse;
import com.haui.borrow.entity.LichSuGiaoDich;
import com.haui.borrow.entity.PhieuMuon;
import com.haui.borrow.repository.LichSuGiaoDichRepository;
import com.haui.borrow.repository.PhieuMuonRepository;
import com.haui.borrow.service.PhieuMuonService;
import com.haui.common.dto.response.ApiResponse;
import com.haui.common.enums.TransactionStatus;
import com.haui.common.exception.AppException;
import com.haui.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PhieuMuonServiceImpl implements PhieuMuonService {

    public static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private final PhieuMuonRepository phieuMuonRepository;
    private final LichSuGiaoDichRepository lichSuGiaoDichRepository;
    private final BookServiceClient bookServiceClient;
    private final CustomerServiceClient customerServiceClient;

    private PhieuMuonResponse toResponse(PhieuMuon entity) {
        String tenSach = entity.getMaSach();
        String tenKH = entity.getMaKH();

        try {
            ApiResponse<BookDto> bookRes = bookServiceClient.getSachById(entity.getMaSach());
            if (bookRes != null && bookRes.getResult() != null && bookRes.getResult().getTenSach() != null) {
                tenSach = bookRes.getResult().getTenSach();
            }
        } catch (Exception ex) {
            log.warn("Could not fetch book details for {}: {}", entity.getMaSach(), ex.getMessage());
        }

        try {
            ApiResponse<CustomerDto> custRes = customerServiceClient.getKhachHangById(entity.getMaKH());
            if (custRes != null && custRes.getResult() != null && custRes.getResult().getTenKH() != null) {
                tenKH = custRes.getResult().getTenKH();
            }
        } catch (Exception ex) {
            log.warn("Could not fetch customer details for {}: {}", entity.getMaKH(), ex.getMessage());
        }

        boolean daQuaHan = entity.getNgayTra() != null && entity.getNgayTra().isBefore(LocalDateTime.now(VIETNAM_ZONE));

        return PhieuMuonResponse.builder()
                .maMuon(entity.getMaMuon())
                .maKH(entity.getMaKH())
                .tenKH(tenKH)
                .maSach(entity.getMaSach())
                .tenSach(tenSach)
                .ngayMuon(entity.getNgayMuon())
                .ngayTra(entity.getNgayTra())
                .soLuong(entity.getSoLuong())
                .daQuaHan(daQuaHan)
                .build();
    }

    private List<PhieuMuonResponse> toResponseList(List<PhieuMuon> list) {
        List<PhieuMuonResponse> responses = new ArrayList<>();
        for (PhieuMuon pm : list) {
            responses.add(toResponse(pm));
        }
        return responses;
    }

    @Override
    public List<PhieuMuonResponse> getAll() {
        return toResponseList(phieuMuonRepository.findAll());
    }

    @Override
    public List<PhieuMuonResponse> getByMaKH(String maKH) {
        return toResponseList(phieuMuonRepository.findByMaKH(maKH));
    }

    @Override
    public PhieuMuonResponse getByMaMuon(String maMuon) {
        PhieuMuon pm = phieuMuonRepository.findById(maMuon)
                .orElseThrow(() -> new AppException(ErrorCode.BORROW_NOT_FOUND));
        return toResponse(pm);
    }

    @Override
    public List<PhieuMuonResponse> getByMaSach(String maSach) {
        return toResponseList(phieuMuonRepository.findByMaSach(maSach));
    }

    @Override
    public List<PhieuMuonResponse> getByNgayMuon(LocalDateTime ngayMuon) {
        LocalDateTime startOfDay = ngayMuon.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = ngayMuon.toLocalDate().atTime(LocalTime.MAX);
        return toResponseList(phieuMuonRepository.findByNgayMuonBetween(startOfDay, endOfDay));
    }

    @Override
    public List<PhieuMuonResponse> getOverdue() {
        return toResponseList(phieuMuonRepository.findByNgayTraBefore(LocalDateTime.now(VIETNAM_ZONE)));
    }

    @Override
    @Transactional
    public PhieuMuonResponse addMuon(PhieuMuonCreationRequest request) {
        if (phieuMuonRepository.existsById(request.getMaMuon())) {
            throw new AppException(ErrorCode.BORROW_EXISTED);
        }

        // 1. Verify customer via Feign
        try {
            ApiResponse<CustomerDto> custRes = customerServiceClient.getKhachHangById(request.getMaKH());
            if (custRes == null || custRes.getResult() == null) {
                throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
            }
        } catch (AppException e) {
            throw e;
        } catch (Exception ex) {
            log.warn("Customer validation warning: {}", ex.getMessage());
        }

        // 2. Verify book & check stock via Feign
        try {
            ApiResponse<BookDto> bookRes = bookServiceClient.getSachById(request.getMaSach());
            if (bookRes == null || bookRes.getResult() == null) {
                throw new AppException(ErrorCode.BOOK_NOT_FOUND);
            }
            if (bookRes.getResult().getSoLuong() < request.getSoLuong()) {
                throw new AppException(ErrorCode.BOOK_INSUFFICIENT_STOCK);
            }
        } catch (AppException e) {
            throw e;
        } catch (Exception ex) {
            log.warn("Book validation warning: {}", ex.getMessage());
        }

        // 3. Deduct stock via Feign
        try {
            bookServiceClient.adjustStock(request.getMaSach(), -request.getSoLuong());
        } catch (Exception ex) {
            log.error("Failed to deduct book stock: {}", ex.getMessage());
        }

        LocalDateTime ngayMuon = request.getNgayMuon() != null ? request.getNgayMuon() : LocalDateTime.now(VIETNAM_ZONE);
        LocalDateTime ngayTra = request.getNgayTra() != null ? request.getNgayTra() : ngayMuon.plusDays(14);

        if (ngayTra.isBefore(ngayMuon)) {
            throw new AppException(ErrorCode.INVALID_BORROW_DATE);
        }

        PhieuMuon phieuMuon = PhieuMuon.builder()
                .maMuon(request.getMaMuon())
                .maKH(request.getMaKH())
                .maSach(request.getMaSach())
                .soLuong(request.getSoLuong())
                .ngayMuon(ngayMuon)
                .ngayTra(ngayTra)
                .build();
        PhieuMuon saved = phieuMuonRepository.save(phieuMuon);

        // Record to transaction history
        String maGD = "GD" + System.currentTimeMillis() % 1000000;
        LichSuGiaoDich lsgd = LichSuGiaoDich.builder()
                .maGD(maGD)
                .ngayGD(LocalDateTime.now(VIETNAM_ZONE))
                .soLuong(request.getSoLuong())
                .trangThai(TransactionStatus.MUON.getDescription())
                .username(request.getUsername())
                .maKH(request.getMaKH())
                .maSach(request.getMaSach())
                .build();
        lichSuGiaoDichRepository.save(lsgd);

        return toResponse(saved);
    }

    @Override
    @Transactional
    public PhieuMuonResponse traSach(TraSachRequest request) {
        PhieuMuon phieuMuon = phieuMuonRepository.findById(request.getMaMuon())
                .orElseThrow(() -> new AppException(ErrorCode.BORROW_NOT_FOUND));

        if (request.getSoLuong() > phieuMuon.getSoLuong()) {
            throw new AppException(ErrorCode.INVALID_RETURN_QUANTITY);
        }

        // Return stock via Feign
        try {
            bookServiceClient.adjustStock(phieuMuon.getMaSach(), request.getSoLuong());
        } catch (Exception ex) {
            log.error("Failed to return book stock: {}", ex.getMessage());
        }

        String maGD = "GD" + System.currentTimeMillis() % 1000000;
        LichSuGiaoDich lsgd = LichSuGiaoDich.builder()
                .maGD(maGD)
                .ngayGD(LocalDateTime.now(VIETNAM_ZONE))
                .soLuong(request.getSoLuong())
                .trangThai(TransactionStatus.TRA.getDescription())
                .username(request.getUsername())
                .maKH(phieuMuon.getMaKH())
                .maSach(phieuMuon.getMaSach())
                .build();
        lichSuGiaoDichRepository.save(lsgd);

        if (request.getSoLuong().equals(phieuMuon.getSoLuong())) {
            phieuMuonRepository.delete(phieuMuon);
            return toResponse(phieuMuon);
        } else {
            phieuMuon.setSoLuong(phieuMuon.getSoLuong() - request.getSoLuong());
            return toResponse(phieuMuonRepository.save(phieuMuon));
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

        String maGD = "GD" + System.currentTimeMillis() % 1000000;
        LichSuGiaoDich lsgd = LichSuGiaoDich.builder()
                .maGD(maGD)
                .ngayGD(LocalDateTime.now(VIETNAM_ZONE))
                .soLuong(request.getSoLuongMat())
                .trangThai(TransactionStatus.MAT.getDescription())
                .username(request.getUsername())
                .maKH(phieuMuon.getMaKH())
                .maSach(phieuMuon.getMaSach())
                .build();
        lichSuGiaoDichRepository.save(lsgd);

        if (request.getSoLuongMat().equals(phieuMuon.getSoLuong())) {
            phieuMuonRepository.delete(phieuMuon);
            return toResponse(phieuMuon);
        } else {
            phieuMuon.setSoLuong(phieuMuon.getSoLuong() - request.getSoLuongMat());
            return toResponse(phieuMuonRepository.save(phieuMuon));
        }
    }
}

package com.haui.library.service.impl;

import com.haui.library.dto.request.KhachHangRequest;
import com.haui.library.dto.response.KhachHangResponse;
import com.haui.library.entity.KhachHang;
import com.haui.library.exception.AppException;
import com.haui.library.exception.ErrorCode;
import com.haui.library.mapper.KhachHangMapper;
import com.haui.library.repository.KhachHangRepository;
import com.haui.library.repository.PhieuMuonRepository;
import com.haui.library.service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KhachHangServiceImpl implements KhachHangService {

    private final KhachHangRepository khachHangRepository;
    private final PhieuMuonRepository phieuMuonRepository;
    private final KhachHangMapper khachHangMapper;

    @Override
    public List<KhachHangResponse> getAll() {
        return khachHangMapper.toResponseList(khachHangRepository.findAll());
    }

    @Override
    public KhachHangResponse getById(String maKH) {
        KhachHang entity = khachHangRepository.findById(maKH)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
        return khachHangMapper.toResponse(entity);
    }

    @Override
    public List<KhachHangResponse> searchByName(String keyword) {
        return khachHangMapper.toResponseList(khachHangRepository.findByTenKHContainingIgnoreCase(keyword));
    }

    @Override
    public List<KhachHangResponse> searchBySDT(String sdt) {
        return khachHangMapper.toResponseList(khachHangRepository.findBySdtKHContaining(sdt));
    }

    @Override
    @Transactional
    public KhachHangResponse add(KhachHangRequest request) {
        if (khachHangRepository.existsById(request.getMaKH())) {
            throw new AppException(ErrorCode.CUSTOMER_EXISTED);
        }
        KhachHang entity = khachHangMapper.toEntity(request);
        return khachHangMapper.toResponse(khachHangRepository.save(entity));
    }

    @Override
    @Transactional
    public KhachHangResponse update(KhachHangRequest request) {
        KhachHang entity = khachHangRepository.findById(request.getMaKH())
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
        khachHangMapper.updateEntityFromRequest(request, entity);
        return khachHangMapper.toResponse(khachHangRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(String maKH) {
        if (!khachHangRepository.existsById(maKH)) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        if (phieuMuonRepository.existsByKhachHang_MaKH(maKH)) {
            throw new AppException(ErrorCode.CUSTOMER_HAS_ACTIVE_BORROW);
        }
        khachHangRepository.deleteById(maKH);
    }
}


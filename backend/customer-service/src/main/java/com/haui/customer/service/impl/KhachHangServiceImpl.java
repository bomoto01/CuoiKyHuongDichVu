package com.haui.customer.service.impl;

import com.haui.common.exception.AppException;
import com.haui.common.exception.ErrorCode;
import com.haui.customer.dto.request.KhachHangRequest;
import com.haui.customer.dto.response.KhachHangResponse;
import com.haui.customer.entity.KhachHang;
import com.haui.customer.mapper.KhachHangMapper;
import com.haui.customer.repository.KhachHangRepository;
import com.haui.customer.service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KhachHangServiceImpl implements KhachHangService {

    private final KhachHangRepository khachHangRepository;
    private final KhachHangMapper khachHangMapper;

    @Override
    public List<KhachHangResponse> getAll() {
        return khachHangMapper.toResponseList(khachHangRepository.findAll());
    }

    @Override
    public KhachHangResponse getById(String maKH) {
        KhachHang khachHang = khachHangRepository.findById(maKH)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
        return khachHangMapper.toResponse(khachHang);
    }

    @Override
    public List<KhachHangResponse> searchByName(String keyword) {
        return khachHangMapper.toResponseList(khachHangRepository.searchByName(keyword));
    }

    @Override
    public List<KhachHangResponse> searchBySDT(String sdt) {
        return khachHangMapper.toResponseList(khachHangRepository.searchBySDT(sdt));
    }

    @Override
    @Transactional
    public KhachHangResponse add(KhachHangRequest request) {
        if (khachHangRepository.existsById(request.getMaKH())) {
            throw new AppException(ErrorCode.CUSTOMER_EXISTED);
        }
        KhachHang khachHang = khachHangMapper.toEntity(request);
        return khachHangMapper.toResponse(khachHangRepository.save(khachHang));
    }

    @Override
    @Transactional
    public KhachHangResponse update(String maKH, KhachHangRequest request) {
        KhachHang khachHang = khachHangRepository.findById(maKH)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
        khachHangMapper.updateEntityFromRequest(request, khachHang);
        return khachHangMapper.toResponse(khachHangRepository.save(khachHang));
    }

    @Override
    @Transactional
    public void delete(String maKH) {
        if (!khachHangRepository.existsById(maKH)) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        khachHangRepository.deleteById(maKH);
    }
}

package com.haui.borrow.service.impl;

import com.haui.borrow.client.BookServiceClient;
import com.haui.borrow.client.CustomerServiceClient;
import com.haui.borrow.client.dto.BookDto;
import com.haui.borrow.client.dto.CustomerDto;
import com.haui.borrow.dto.response.LichSuGiaoDichResponse;
import com.haui.borrow.entity.LichSuGiaoDich;
import com.haui.borrow.repository.LichSuGiaoDichRepository;
import com.haui.borrow.service.LichSuGiaoDichService;
import com.haui.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LichSuGiaoDichServiceImpl implements LichSuGiaoDichService {

    public static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private final LichSuGiaoDichRepository lichSuGiaoDichRepository;
    private final BookServiceClient bookServiceClient;
    private final CustomerServiceClient customerServiceClient;

    private LichSuGiaoDichResponse toResponse(LichSuGiaoDich entity) {
        String tenSach = entity.getMaSach();
        String tenKH = entity.getMaKH();

        if (entity.getMaSach() != null) {
            try {
                ApiResponse<BookDto> res = bookServiceClient.getSachById(entity.getMaSach());
                if (res != null && res.getResult() != null && res.getResult().getTenSach() != null) {
                    tenSach = res.getResult().getTenSach();
                }
            } catch (Exception ex) {
                log.warn("Could not fetch book details: {}", ex.getMessage());
            }
        }

        if (entity.getMaKH() != null) {
            try {
                ApiResponse<CustomerDto> res = customerServiceClient.getKhachHangById(entity.getMaKH());
                if (res != null && res.getResult() != null && res.getResult().getTenKH() != null) {
                    tenKH = res.getResult().getTenKH();
                }
            } catch (Exception ex) {
                log.warn("Could not fetch customer details: {}", ex.getMessage());
            }
        }

        return LichSuGiaoDichResponse.builder()
                .maGD(entity.getMaGD())
                .ngayGD(entity.getNgayGD())
                .soLuong(entity.getSoLuong())
                .trangThai(entity.getTrangThai())
                .username(entity.getUsername())
                .tenTK(entity.getUsername())
                .maKH(entity.getMaKH())
                .tenKH(tenKH)
                .maSach(entity.getMaSach())
                .tenSach(tenSach)
                .build();
    }

    private List<LichSuGiaoDichResponse> toResponseList(List<LichSuGiaoDich> list) {
        List<LichSuGiaoDichResponse> responses = new ArrayList<>();
        for (LichSuGiaoDich gd : list) {
            responses.add(toResponse(gd));
        }
        return responses;
    }

    @Override
    public List<LichSuGiaoDichResponse> getAll() {
        return toResponseList(lichSuGiaoDichRepository.findAll());
    }

    @Override
    public List<LichSuGiaoDichResponse> getByUsername(String username) {
        return toResponseList(lichSuGiaoDichRepository.findByUsername(username));
    }

    @Override
    public List<LichSuGiaoDichResponse> getByMaSach(String maSach) {
        return toResponseList(lichSuGiaoDichRepository.findByMaSach(maSach));
    }

    @Override
    public List<LichSuGiaoDichResponse> getByNgayGD(LocalDateTime ngayGDDau, LocalDateTime ngayGDCuoi) {
        if (ngayGDDau == null) {
            ngayGDDau = LocalDateTime.of(2000, 1, 1, 0, 0);
        }
        if (ngayGDCuoi == null) {
            ngayGDCuoi = LocalDateTime.now(VIETNAM_ZONE);
        }
        if (ngayGDDau.isAfter(ngayGDCuoi)) {
            LocalDateTime temp = ngayGDDau;
            ngayGDDau = ngayGDCuoi;
            ngayGDCuoi = temp;
        }
        return toResponseList(lichSuGiaoDichRepository.findByNgayGDBetween(ngayGDDau, ngayGDCuoi));
    }

    @Override
    public List<LichSuGiaoDichResponse> getByTrangThai(String trangThai) {
        return toResponseList(lichSuGiaoDichRepository.findByTrangThai(trangThai));
    }

    @Override
    @Transactional
    public int deleteOlderThanThreeYears() {
        LocalDateTime cutOff = LocalDateTime.now(VIETNAM_ZONE).minusYears(3);
        return lichSuGiaoDichRepository.deleteByNgayGDBefore(cutOff);
    }
}

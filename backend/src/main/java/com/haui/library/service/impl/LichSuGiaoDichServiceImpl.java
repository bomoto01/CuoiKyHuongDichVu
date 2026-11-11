package com.haui.library.service.impl;

import com.haui.library.dto.response.LichSuGiaoDichResponse;
import com.haui.library.mapper.LichSuGiaoDichMapper;
import com.haui.library.repository.LichSuGiaoDichRepository;
import com.haui.library.service.LichSuGiaoDichService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LichSuGiaoDichServiceImpl implements LichSuGiaoDichService {

    private final LichSuGiaoDichRepository lichSuGiaoDichRepository;
    private final LichSuGiaoDichMapper lichSuGiaoDichMapper;

    @Override
    public List<LichSuGiaoDichResponse> getAll() {
        return lichSuGiaoDichMapper.toResponseList(lichSuGiaoDichRepository.findAll());
    }

    @Override
    public List<LichSuGiaoDichResponse> getByUsername(String username) {
        return lichSuGiaoDichMapper.toResponseList(lichSuGiaoDichRepository.findByTaiKhoan_Username(username));
    }

    @Override
    public List<LichSuGiaoDichResponse> getByMaSach(String maSach) {
        return lichSuGiaoDichMapper.toResponseList(lichSuGiaoDichRepository.findBySach_MaSach(maSach));
    }

    @Override
    public List<LichSuGiaoDichResponse> getByNgayGD(LocalDateTime ngayGDDau, LocalDateTime ngayGDCuoi) {
        if (ngayGDDau == null) {
            ngayGDDau = LocalDateTime.of(2000, 1, 1, 0, 0);
        }
        if (ngayGDCuoi == null) {
            ngayGDCuoi = LocalDateTime.now();
        }
        // ensure start is before end
        if (ngayGDDau.isAfter(ngayGDCuoi)) {
            LocalDateTime temp = ngayGDDau;
            ngayGDDau = ngayGDCuoi;
            ngayGDCuoi = temp;
        }
        return lichSuGiaoDichMapper.toResponseList(lichSuGiaoDichRepository.findByNgayGDBetween(ngayGDDau, ngayGDCuoi));
    }

    @Override
    public List<LichSuGiaoDichResponse> getByTrangThai(String trangThai) {
        return lichSuGiaoDichMapper.toResponseList(lichSuGiaoDichRepository.findByTrangThai(trangThai));
    }

    @Override
    @Transactional
    public int deleteOlderThanThreeYears() {
        LocalDateTime cutOff = LocalDateTime.now().minusYears(3);
        return lichSuGiaoDichRepository.deleteByNgayGDBefore(cutOff);
    }
}


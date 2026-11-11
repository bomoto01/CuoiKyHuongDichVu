package com.haui.library.service.impl;

import com.haui.library.dto.request.SachRequest;
import com.haui.library.dto.response.SachResponse;
import com.haui.library.entity.LichSuGiaoDich;
import com.haui.library.entity.Sach;
import com.haui.library.entity.TaiKhoan;
import com.haui.library.entity.TheLoai;
import com.haui.library.enums.TransactionStatus;
import com.haui.library.exception.AppException;
import com.haui.library.exception.ErrorCode;
import com.haui.library.mapper.SachMapper;
import com.haui.library.repository.LichSuGiaoDichRepository;
import com.haui.library.repository.PhieuMuonRepository;
import com.haui.library.repository.SachRepository;
import com.haui.library.repository.TaiKhoanRepository;
import com.haui.library.repository.TheLoaiRepository;
import com.haui.library.service.SachService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SachServiceImpl implements SachService {

    private final SachRepository sachRepository;
    private final TheLoaiRepository theLoaiRepository;
    private final PhieuMuonRepository phieuMuonRepository;
    private final LichSuGiaoDichRepository lichSuGiaoDichRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final SachMapper sachMapper;

    @Override
    public List<SachResponse> getAll() {
        return sachMapper.toResponseList(sachRepository.findAll());
    }

    @Override
    public SachResponse getById(String maSach) {
        Sach sach = sachRepository.findById(maSach)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        return sachMapper.toResponse(sach);
    }

    @Override
    public List<SachResponse> getByTheLoai(String maTheLoai) {
        return sachMapper.toResponseList(sachRepository.findByTheLoai_MaTheLoai(maTheLoai));
    }

    @Override
    public List<SachResponse> searchByName(String keyword) {
        return sachMapper.toResponseList(sachRepository.findByTenSachContainingIgnoreCase(keyword));
    }

    @Override
    @Transactional
    public SachResponse add(SachRequest request) {
        if (sachRepository.existsById(request.getMaSach())) {
            throw new AppException(ErrorCode.BOOK_EXISTED);
        }

        TheLoai theLoai = theLoaiRepository.findById(request.getMaTheLoai())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Sach sach = sachMapper.toEntity(request);
        sach.setTheLoai(theLoai);
        return sachMapper.toResponse(sachRepository.save(sach));
    }

    @Override
    @Transactional
    public SachResponse update(SachRequest request) {
        Sach sach = sachRepository.findById(request.getMaSach())
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        TheLoai theLoai = theLoaiRepository.findById(request.getMaTheLoai())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        sachMapper.updateEntityFromRequest(request, sach);
        sach.setTheLoai(theLoai);
        return sachMapper.toResponse(sachRepository.save(sach));
    }

    @Override
    @Transactional
    public SachResponse addMore(String maSach, Integer soLuongThem) {
        if (soLuongThem == null || soLuongThem <= 0) {
            throw new AppException(ErrorCode.INVALID_BOOK_QUANTITY);
        }

        Sach sach = sachRepository.findById(maSach)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        sach.setSoLuong(sach.getSoLuong() + soLuongThem);
        return sachMapper.toResponse(sachRepository.save(sach));
    }

    @Override
    @Transactional
    public SachResponse removeSome(String maSach, Integer soLuongXoa, String username) {
        if (soLuongXoa == null || soLuongXoa <= 0) {
            throw new AppException(ErrorCode.INVALID_BOOK_QUANTITY);
        }

        Sach sach = sachRepository.findById(maSach)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        if (sach.getSoLuong() < soLuongXoa) {
            throw new AppException(ErrorCode.BOOK_INSUFFICIENT_STOCK);
        }

        sach.setSoLuong(sach.getSoLuong() - soLuongXoa);
        sachRepository.save(sach);

        // Record into LichSuGiaoDich
        TaiKhoan taiKhoan = null;
        if (username != null && !username.isBlank()) {
            taiKhoan = taiKhoanRepository.findByUsername(username).orElse(null);
        }

        String maGD = "GD" + System.currentTimeMillis() % 1000000;
        LichSuGiaoDich lsgd = LichSuGiaoDich.builder()
                .maGD(maGD)
                .ngayGD(LocalDateTime.now())
                .soLuong(soLuongXoa)
                .trangThai(TransactionStatus.HUY_SACH.getDescription())
                .sach(sach)
                .taiKhoan(taiKhoan)
                .build();
        lichSuGiaoDichRepository.save(lsgd);

        return sachMapper.toResponse(sach);
    }

    @Override
    @Transactional
    public void delete(String maSach) {
        if (!sachRepository.existsById(maSach)) {
            throw new AppException(ErrorCode.BOOK_NOT_FOUND);
        }

        if (phieuMuonRepository.existsBySach_MaSach(maSach)) {
            throw new AppException(ErrorCode.BOOK_BEING_BORROWED);
        }

        sachRepository.deleteById(maSach);
    }
}

package com.haui.book.service.impl;

import com.haui.book.dto.request.SachRequest;
import com.haui.book.dto.response.SachResponse;
import com.haui.book.entity.Sach;
import com.haui.book.entity.TheLoai;
import com.haui.book.mapper.SachMapper;
import com.haui.book.repository.SachRepository;
import com.haui.book.repository.TheLoaiRepository;
import com.haui.book.service.SachService;
import com.haui.common.exception.AppException;
import com.haui.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SachServiceImpl implements SachService {

    private final SachRepository sachRepository;
    private final TheLoaiRepository theLoaiRepository;
    private final SachMapper sachMapper;

    @Override
    public List<SachResponse> getAll() {
        return sachMapper.toResponseList(sachRepository.findAllWithTheLoai());
    }

    @Override
    public SachResponse getById(String maSach) {
        Sach sach = sachRepository.findByIdWithTheLoai(maSach)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        return sachMapper.toResponse(sach);
    }

    @Override
    public List<SachResponse> getByTheLoai(String maTheLoai) {
        return sachMapper.toResponseList(sachRepository.findByTheLoai_MaTheLoai(maTheLoai));
    }

    @Override
    public List<SachResponse> searchByName(String keyword) {
        return sachMapper.toResponseList(sachRepository.searchByName(keyword));
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
    public SachResponse update(String maSach, SachRequest request) {
        Sach sach = sachRepository.findByIdWithTheLoai(maSach)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        sachMapper.updateEntityFromRequest(request, sach);

        if (request.getMaTheLoai() != null && !request.getMaTheLoai().isBlank()) {
            TheLoai theLoai = theLoaiRepository.findById(request.getMaTheLoai())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            sach.setTheLoai(theLoai);
        }

        return sachMapper.toResponse(sachRepository.save(sach));
    }

    @Override
    @Transactional
    public void delete(String maSach) {
        if (!sachRepository.existsById(maSach)) {
            throw new AppException(ErrorCode.BOOK_NOT_FOUND);
        }
        sachRepository.deleteById(maSach);
    }

    @Override
    @Transactional
    public SachResponse huySach(String maSach, Integer soLuongHuy, String username) {
        Sach sach = sachRepository.findByIdWithTheLoai(maSach)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        if (soLuongHuy == null || soLuongHuy <= 0 || soLuongHuy > sach.getSoLuong()) {
            throw new AppException(ErrorCode.INVALID_LOST_QUANTITY);
        }

        sach.setSoLuong(sach.getSoLuong() - soLuongHuy);
        return sachMapper.toResponse(sachRepository.save(sach));
    }

    @Override
    @Transactional
    public SachResponse adjustStock(String maSach, Integer delta) {
        Sach sach = sachRepository.findByIdWithTheLoai(maSach)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        int newStock = sach.getSoLuong() + delta;
        if (newStock < 0) {
            throw new AppException(ErrorCode.BOOK_INSUFFICIENT_STOCK);
        }
        sach.setSoLuong(newStock);
        return sachMapper.toResponse(sachRepository.save(sach));
    }
}

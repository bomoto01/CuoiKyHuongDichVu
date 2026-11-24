package com.haui.book.service.impl;

import com.haui.book.dto.request.TheLoaiRequest;
import com.haui.book.dto.response.TheLoaiResponse;
import com.haui.book.entity.TheLoai;
import com.haui.book.mapper.TheLoaiMapper;
import com.haui.book.repository.TheLoaiRepository;
import com.haui.book.service.TheLoaiService;
import com.haui.common.exception.AppException;
import com.haui.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TheLoaiServiceImpl implements TheLoaiService {

    private final TheLoaiRepository theLoaiRepository;
    private final TheLoaiMapper theLoaiMapper;

    @Override
    public List<TheLoaiResponse> getAll() {
        return theLoaiMapper.toResponseList(theLoaiRepository.findAll());
    }

    @Override
    public TheLoaiResponse getById(String maTheLoai) {
        TheLoai theLoai = theLoaiRepository.findById(maTheLoai)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return theLoaiMapper.toResponse(theLoai);
    }

    @Override
    public List<TheLoaiResponse> searchByName(String keyword) {
        return theLoaiMapper.toResponseList(theLoaiRepository.searchByName(keyword));
    }

    @Override
    @Transactional
    public TheLoaiResponse add(TheLoaiRequest request) {
        if (theLoaiRepository.existsById(request.getMaTheLoai())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        TheLoai theLoai = theLoaiMapper.toEntity(request);
        return theLoaiMapper.toResponse(theLoaiRepository.save(theLoai));
    }

    @Override
    @Transactional
    public TheLoaiResponse update(String maTheLoai, TheLoaiRequest request) {
        TheLoai theLoai = theLoaiRepository.findById(maTheLoai)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        theLoaiMapper.updateEntityFromRequest(request, theLoai);
        return theLoaiMapper.toResponse(theLoaiRepository.save(theLoai));
    }

    @Override
    @Transactional
    public void delete(String maTheLoai) {
        TheLoai theLoai = theLoaiRepository.findById(maTheLoai)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        if (theLoai.getSachList() != null && !theLoai.getSachList().isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_HAS_BOOKS);
        }
        theLoaiRepository.deleteById(maTheLoai);
    }
}

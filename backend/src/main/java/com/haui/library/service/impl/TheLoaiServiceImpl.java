package com.haui.library.service.impl;

import com.haui.library.dto.request.TheLoaiRequest;
import com.haui.library.dto.response.TheLoaiResponse;
import com.haui.library.entity.TheLoai;
import com.haui.library.exception.AppException;
import com.haui.library.exception.ErrorCode;
import com.haui.library.mapper.TheLoaiMapper;
import com.haui.library.repository.SachRepository;
import com.haui.library.repository.TheLoaiRepository;
import com.haui.library.service.TheLoaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheLoaiServiceImpl implements TheLoaiService {

    private final TheLoaiRepository theLoaiRepository;
    private final SachRepository sachRepository;
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
        return theLoaiMapper.toResponseList(theLoaiRepository.findByTenTheLoaiContainingIgnoreCase(keyword));
    }

    @Override
    @Transactional
    public TheLoaiResponse add(TheLoaiRequest request) {
        if (theLoaiRepository.existsByMaTheLoai(request.getMaTheLoai())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        TheLoai entity = theLoaiMapper.toEntity(request);
        return theLoaiMapper.toResponse(theLoaiRepository.save(entity));
    }

    @Override
    @Transactional
    public TheLoaiResponse update(TheLoaiRequest request) {
        TheLoai entity = theLoaiRepository.findById(request.getMaTheLoai())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        theLoaiMapper.updateEntityFromRequest(request, entity);
        return theLoaiMapper.toResponse(theLoaiRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(String maTheLoai) {
        if (!theLoaiRepository.existsById(maTheLoai)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        if (sachRepository.existsByTheLoai_MaTheLoai(maTheLoai)) {
            throw new AppException(ErrorCode.CATEGORY_HAS_BOOKS);
        }
        theLoaiRepository.deleteById(maTheLoai);
    }
}


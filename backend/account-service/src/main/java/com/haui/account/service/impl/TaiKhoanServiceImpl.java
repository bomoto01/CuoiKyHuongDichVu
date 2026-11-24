package com.haui.account.service.impl;

import com.haui.account.dto.request.TaiKhoanRequest;
import com.haui.account.dto.response.TaiKhoanResponse;
import com.haui.account.entity.TaiKhoan;
import com.haui.account.mapper.TaiKhoanMapper;
import com.haui.account.repository.TaiKhoanRepository;
import com.haui.account.service.TaiKhoanService;
import com.haui.common.exception.AppException;
import com.haui.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaiKhoanServiceImpl implements TaiKhoanService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final TaiKhoanMapper taiKhoanMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<TaiKhoanResponse> getAll() {
        return taiKhoanMapper.toResponseList(taiKhoanRepository.findAll());
    }

    @Override
    public TaiKhoanResponse getByUsername(String username) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return taiKhoanMapper.toResponse(taiKhoan);
    }

    @Override
    public List<TaiKhoanResponse> searchByNameOrUsername(String keyword) {
        return taiKhoanMapper.toResponseList(taiKhoanRepository.searchByNameOrUsername(keyword));
    }

    @Override
    @Transactional
    public TaiKhoanResponse add(TaiKhoanRequest request) {
        if (taiKhoanRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        TaiKhoan taiKhoan = taiKhoanMapper.toEntity(request);
        taiKhoan.setPass(passwordEncoder.encode(request.getPass()));
        TaiKhoan saved = taiKhoanRepository.save(taiKhoan);
        return taiKhoanMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TaiKhoanResponse update(String username, TaiKhoanRequest request) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        taiKhoanMapper.updateEntityFromRequest(request, taiKhoan);
        if (request.getPass() != null && !request.getPass().isBlank()) {
            taiKhoan.setPass(passwordEncoder.encode(request.getPass()));
        }
        return taiKhoanMapper.toResponse(taiKhoanRepository.save(taiKhoan));
    }

    @Override
    @Transactional
    public void delete(String username) {
        if (!taiKhoanRepository.existsByUsername(username)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        taiKhoanRepository.deleteById(username);
    }
}

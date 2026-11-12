package com.haui.library.service.impl;

import com.haui.library.dto.request.TaiKhoanRequest;
import com.haui.library.dto.response.TaiKhoanResponse;
import com.haui.library.entity.TaiKhoan;
import com.haui.library.enums.Role;
import com.haui.library.exception.AppException;
import com.haui.library.exception.ErrorCode;
import com.haui.library.mapper.TaiKhoanMapper;
import com.haui.library.repository.TaiKhoanRepository;
import com.haui.library.service.TaiKhoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
        TaiKhoan entity = taiKhoanRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return taiKhoanMapper.toResponse(entity);
    }

    @Override
    public List<TaiKhoanResponse> searchByNameOrUsername(String keyword) {
        return taiKhoanMapper.toResponseList(
                taiKhoanRepository.findByUsernameContainingIgnoreCaseOrTenTKContainingIgnoreCase(keyword, keyword)
        );
    }

    @Override
    @Transactional
    public TaiKhoanResponse add(TaiKhoanRequest request) {
        if (taiKhoanRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        TaiKhoan entity = taiKhoanMapper.toEntity(request);
        entity.setPass(passwordEncoder.encode(request.getPass() != null ? request.getPass() : "123456"));
        if (entity.getRole() == null) {
            entity.setRole(Role.MANAGER);
        }
        return taiKhoanMapper.toResponse(taiKhoanRepository.save(entity));
    }

    @Override
    @Transactional
    public TaiKhoanResponse update(TaiKhoanRequest request) {
        TaiKhoan entity = taiKhoanRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        taiKhoanMapper.updateEntityFromRequest(request, entity);
        if (StringUtils.hasText(request.getPass())) {
            entity.setPass(passwordEncoder.encode(request.getPass()));
        }
        if (request.getRole() != null) {
            entity.setRole(request.getRole());
        }

        return taiKhoanMapper.toResponse(taiKhoanRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(String username) {
        if (!taiKhoanRepository.existsById(username)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        taiKhoanRepository.deleteById(username);
    }
}


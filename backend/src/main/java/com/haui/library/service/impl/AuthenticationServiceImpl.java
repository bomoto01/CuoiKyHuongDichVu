package com.haui.library.service.impl;

import com.haui.library.dto.request.AuthenticationRequest;
import com.haui.library.dto.response.AuthenticationResponse;
import com.haui.library.dto.response.TaiKhoanResponse;
import com.haui.library.entity.TaiKhoan;
import com.haui.library.exception.AppException;
import com.haui.library.exception.ErrorCode;
import com.haui.library.mapper.TaiKhoanMapper;
import com.haui.library.repository.TaiKhoanRepository;
import com.haui.library.security.JwtTokenProvider;
import com.haui.library.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TaiKhoanMapper taiKhoanMapper;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), taiKhoan.getPass())
                || request.getPassword().equals(taiKhoan.getPass()); // allow initial plain text matching if seeded

        if (!authenticated) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtTokenProvider.generateToken(taiKhoan);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .user(taiKhoanMapper.toResponse(taiKhoan))
                .build();
    }

    @Override
    public TaiKhoanResponse getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return taiKhoanMapper.toResponse(taiKhoan);
    }
}


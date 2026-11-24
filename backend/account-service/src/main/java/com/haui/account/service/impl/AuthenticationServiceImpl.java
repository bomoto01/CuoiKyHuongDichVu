package com.haui.account.service.impl;

import com.haui.account.dto.request.AuthenticationRequest;
import com.haui.account.dto.response.AuthenticationResponse;
import com.haui.account.dto.response.TaiKhoanResponse;
import com.haui.account.entity.TaiKhoan;
import com.haui.account.mapper.TaiKhoanMapper;
import com.haui.account.repository.TaiKhoanRepository;
import com.haui.account.service.AuthenticationService;
import com.haui.common.exception.AppException;
import com.haui.common.exception.ErrorCode;
import com.haui.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

        boolean authenticated = passwordEncoder.matches(request.getPassword(), taiKhoan.getPass());
        if (!authenticated && request.getPassword().equals(taiKhoan.getPass())) {
            authenticated = true;
            taiKhoan.setPass(passwordEncoder.encode(request.getPassword()));
            taiKhoanRepository.save(taiKhoan);
        }

        if (!authenticated) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtTokenProvider.generateToken(taiKhoan.getUsername(), taiKhoan.getRole().name(), taiKhoan.getTenTK());

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .username(taiKhoan.getUsername())
                .tenTK(taiKhoan.getTenTK())
                .role(taiKhoan.getRole())
                .build();
    }

    @Override
    public TaiKhoanResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return taiKhoanMapper.toResponse(taiKhoan);
    }
}

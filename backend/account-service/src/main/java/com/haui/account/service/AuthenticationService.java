package com.haui.account.service;

import com.haui.account.dto.request.AuthenticationRequest;
import com.haui.account.dto.response.AuthenticationResponse;
import com.haui.account.dto.response.TaiKhoanResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    TaiKhoanResponse getMyInfo();
}

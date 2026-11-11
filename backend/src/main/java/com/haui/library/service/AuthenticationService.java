package com.haui.library.service;

import com.haui.library.dto.request.AuthenticationRequest;
import com.haui.library.dto.response.AuthenticationResponse;
import com.haui.library.dto.response.TaiKhoanResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    TaiKhoanResponse getMyInfo();
}

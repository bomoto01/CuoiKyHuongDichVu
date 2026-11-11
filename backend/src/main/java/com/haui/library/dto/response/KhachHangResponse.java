package com.haui.library.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHangResponse {
    private String maKH;
    private String tenKH;
    private String sdtKH;
}


package com.haui.book.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheLoaiResponse {
    private String maTheLoai;
    private String tenTheLoai;
}

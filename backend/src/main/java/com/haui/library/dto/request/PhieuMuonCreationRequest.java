package com.haui.library.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhieuMuonCreationRequest {
    @NotBlank(message = "Mã mượn không được để trống")
    private String maMuon;

    @NotBlank(message = "Mã khách hàng không được để trống")
    private String maKH;

    @NotBlank(message = "Mã sách không được để trống")
    private String maSach;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer soLuong;

    private LocalDateTime ngayMuon;

    private LocalDateTime ngayTra;

    private String username;
}

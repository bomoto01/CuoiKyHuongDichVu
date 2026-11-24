package com.haui.borrow.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraSachRequest {
    @NotBlank(message = "Mã phiếu mượn không được để trống")
    private String maMuon;

    @NotNull(message = "Số lượng trả không được để trống")
    @Min(value = 1, message = "Số lượng trả phải lớn hơn 0")
    private Integer soLuong;

    private String username;
}

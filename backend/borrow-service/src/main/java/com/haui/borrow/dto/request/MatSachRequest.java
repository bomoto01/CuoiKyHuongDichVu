package com.haui.borrow.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatSachRequest {
    @NotBlank(message = "Mã phiếu mượn không được để trống")
    private String maMuon;

    @NotNull(message = "Số lượng mất không được để trống")
    @Min(value = 1, message = "Số lượng mất phải lớn hơn 0")
    private Integer soLuongMat;

    private String username;
}

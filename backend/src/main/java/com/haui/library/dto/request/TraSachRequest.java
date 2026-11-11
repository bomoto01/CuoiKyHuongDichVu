package com.haui.library.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraSachRequest {
    @NotBlank(message = "Mã mượn không được để trống")
    private String maMuon;

    private String username;

    @NotNull(message = "Số lượng trả không được để trống")
    @Min(value = 1, message = "Số lượng trả phải lớn hơn 0")
    private Integer soLuong;
}

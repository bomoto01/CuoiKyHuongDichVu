package com.haui.borrow.client.dto;

import lombok.Data;

@Data
public class BookDto {
    private String maSach;
    private String tenSach;
    private Integer soLuong;
    private String tacGia;
    private String maTheLoai;
    private String tenTheLoai;
}

package com.haui.borrow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "LichSuGiaoDich")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuGiaoDich {

    @Id
    @Column(name = "MaGD", length = 10, nullable = false)
    private String maGD;

    @Column(name = "NgayGD", nullable = false)
    private LocalDateTime ngayGD;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "TrangThai", length = 50)
    private String trangThai;

    @Column(name = "Username", length = 50)
    private String username;

    @Column(name = "MaKH", length = 10)
    private String maKH;

    @Column(name = "MaSach", length = 10)
    private String maSach;
}

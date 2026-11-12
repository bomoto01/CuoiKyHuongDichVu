package com.haui.library.entity;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Username")
    private TaiKhoan taiKhoan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaKH")
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaSach")
    private Sach sach;
}


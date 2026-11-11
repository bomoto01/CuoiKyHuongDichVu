package com.haui.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PhieuMuon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhieuMuon {

    @Id
    @Column(name = "MaMuon", length = 10, nullable = false)
    private String maMuon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKH", nullable = false)
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaSach", nullable = false)
    private Sach sach;

    @Column(name = "NgayMuon")
    private LocalDateTime ngayMuon;

    @Column(name = "NgayTra")
    private LocalDateTime ngayTra;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;
}


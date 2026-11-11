package com.haui.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "KhachHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHang {

    @Id
    @Column(name = "MaKH", length = 10, nullable = false)
    private String maKH;

    @Column(name = "TenKH", length = 100, nullable = false)
    private String tenKH;

    @Column(name = "SdtKH", length = 15)
    private String sdtKH;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhieuMuon> phieuMuonList;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LichSuGiaoDich> lichSuGiaoDichList;
}

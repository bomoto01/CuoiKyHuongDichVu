package com.haui.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Sach")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sach {

    @Id
    @Column(name = "MaSach", length = 10, nullable = false)
    private String maSach;

    @Column(name = "TenSach", length = 200, nullable = false)
    private String tenSach;

    @Column(name = "TacGia", length = 100)
    private String tacGia;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTheLoai")
    private TheLoai theLoai;

    @OneToMany(mappedBy = "sach", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhieuMuon> phieuMuonList;

    @OneToMany(mappedBy = "sach", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LichSuGiaoDich> lichSuGiaoDichList;
}

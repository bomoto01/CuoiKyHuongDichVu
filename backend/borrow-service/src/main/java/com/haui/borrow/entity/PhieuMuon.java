package com.haui.borrow.entity;

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

    @Column(name = "MaKH", length = 10, nullable = false)
    private String maKH;

    @Column(name = "MaSach", length = 10, nullable = false)
    private String maSach;

    @Column(name = "NgayMuon")
    private LocalDateTime ngayMuon;

    @Column(name = "NgayTra")
    private LocalDateTime ngayTra;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;
}

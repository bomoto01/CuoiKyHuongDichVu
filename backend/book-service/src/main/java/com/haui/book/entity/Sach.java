package com.haui.book.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "TacGia", length = 100)
    private String tacGia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTheLoai", nullable = false)
    private TheLoai theLoai;
}

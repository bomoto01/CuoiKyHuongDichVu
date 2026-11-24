package com.haui.customer.entity;

import jakarta.persistence.*;
import lombok.*;

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
}

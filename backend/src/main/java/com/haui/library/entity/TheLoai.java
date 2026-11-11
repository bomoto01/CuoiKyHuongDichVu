package com.haui.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "TheLoai")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheLoai {

    @Id
    @Column(name = "MaTheLoai", length = 10, nullable = false)
    private String maTheLoai;

    @Column(name = "TenTheLoai", length = 100, nullable = false)
    private String tenTheLoai;

    @OneToMany(mappedBy = "theLoai", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sach> sachList;
}


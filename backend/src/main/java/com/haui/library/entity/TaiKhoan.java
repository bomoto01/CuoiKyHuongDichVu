package com.haui.library.entity;

import com.haui.library.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "TaiKhoan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiKhoan {

    @Id
    @Column(name = "Username", length = 50, nullable = false)
    private String username;

    @Column(name = "Pass", length = 100, nullable = false)
    private String pass;

    @Column(name = "TenTK", length = 100)
    private String tenTK;

    @Column(name = "SdtTK", length = 15)
    private String sdtTK;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", length = 20, nullable = false)
    private Role role;

    @OneToMany(mappedBy = "taiKhoan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LichSuGiaoDich> lichSuGiaoDichList;
}

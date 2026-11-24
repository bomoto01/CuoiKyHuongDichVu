package com.haui.account.config;

import com.haui.account.entity.TaiKhoan;
import com.haui.account.repository.TaiKhoanRepository;
import com.haui.common.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AccountInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner accountRunner(TaiKhoanRepository taiKhoanRepository) {
        return args -> {
            if (taiKhoanRepository.count() == 0) {
                log.info("Seeding default accounts for Account Service...");
                TaiKhoan admin = TaiKhoan.builder()
                        .username("admin")
                        .pass(passwordEncoder.encode("123456"))
                        .tenTK("Admin")
                        .sdtTK("0988888888")
                        .role(Role.ADMIN)
                        .build();

                TaiKhoan manager = TaiKhoan.builder()
                        .username("phamquoctoan")
                        .pass(passwordEncoder.encode("123456"))
                        .tenTK("Phạm Quốc Toản")
                        .sdtTK("0347162755")
                        .role(Role.MANAGER)
                        .build();

                taiKhoanRepository.save(admin);
                taiKhoanRepository.save(manager);
                log.info("Seeded default accounts: admin and phamquoctoan");
            }
        };
    }
}

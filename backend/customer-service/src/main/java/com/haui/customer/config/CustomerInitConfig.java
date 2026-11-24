package com.haui.customer.config;

import com.haui.customer.entity.KhachHang;
import com.haui.customer.repository.KhachHangRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CustomerInitConfig {

    @Bean
    public ApplicationRunner customerRunner(KhachHangRepository khachHangRepository) {
        return args -> {
            if (khachHangRepository.count() == 0) {
                log.info("Seeding default customers for Customer Service...");
                KhachHang kh1 = KhachHang.builder().maKH("KH125583").tenKH("Bùi Minh Tâm").sdtKH("0987654321").build();
                KhachHang kh2 = KhachHang.builder().maKH("KH252642").tenKH("Phạm Quốc Toản").sdtKH("0123456789").build();
                KhachHang kh3 = KhachHang.builder().maKH("KH831056").tenKH("Nguyễn Như Hoàn").sdtKH("0347162755").build();

                khachHangRepository.save(kh1);
                khachHangRepository.save(kh2);
                khachHangRepository.save(kh3);
                log.info("Seeded 3 default customers.");
            }
        };
    }
}

package com.haui.book.config;

import com.haui.book.entity.Sach;
import com.haui.book.entity.TheLoai;
import com.haui.book.repository.SachRepository;
import com.haui.book.repository.TheLoaiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BookInitConfig {

    @Bean
    public ApplicationRunner bookRunner(TheLoaiRepository theLoaiRepository, SachRepository sachRepository) {
        return args -> {
            if (theLoaiRepository.count() == 0) {
                log.info("Seeding default categories for Book Service...");
                TheLoai tl1 = TheLoai.builder().maTheLoai("TL001").tenTheLoai("Sách giáo khoa").build();
                TheLoai tl2 = TheLoai.builder().maTheLoai("TL002").tenTheLoai("Tiểu thuyết").build();
                TheLoai tl3 = TheLoai.builder().maTheLoai("TL003").tenTheLoai("Khoa học khám phá").build();
                TheLoai tl4 = TheLoai.builder().maTheLoai("TL004").tenTheLoai("Công nghệ thông tin").build();

                theLoaiRepository.save(tl1);
                theLoaiRepository.save(tl2);
                theLoaiRepository.save(tl3);
                theLoaiRepository.save(tl4);

                if (sachRepository.count() == 0) {
                    log.info("Seeding default books for Book Service...");
                    Sach s1 = Sach.builder().maSach("S001").tenSach("Toán lớp 10").tacGia("Huỳnh Nhật Minh").soLuong(9).theLoai(tl1).build();
                    Sach s2 = Sach.builder().maSach("S002").tenSach("Vì sao lại thế").tacGia("Hoàng Mai").soLuong(5).theLoai(tl3).build();
                    Sach s3 = Sach.builder().maSach("S003").tenSach("Sao tháng tám").tacGia("Nguyễn Nhật Ánh").soLuong(10).theLoai(tl2).build();
                    Sach s4 = Sach.builder().maSach("S004").tenSach("Văn lớp 3").tacGia("Như Hoàn").soLuong(20).theLoai(tl2).build();
                    Sach s5 = Sach.builder().maSach("S005").tenSach("Lập trình Web Spring Boot & React").tacGia("Trần Thanh Hùng").soLuong(15).theLoai(tl4).build();

                    sachRepository.save(s1);
                    sachRepository.save(s2);
                    sachRepository.save(s3);
                    sachRepository.save(s4);
                    sachRepository.save(s5);
                }
            }
        };
    }
}

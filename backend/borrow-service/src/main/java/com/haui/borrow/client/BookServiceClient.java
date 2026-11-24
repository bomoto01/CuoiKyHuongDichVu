package com.haui.borrow.client;

import com.haui.borrow.client.dto.BookDto;
import com.haui.common.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "book-service", url = "${services.book-service.url:http://localhost:8082}")
public interface BookServiceClient {

    @GetMapping("/api/Sach/GetSachById")
    ApiResponse<BookDto> getSachById(@RequestParam("MaSach") String maSach);

    @PostMapping("/api/Sach/adjust-stock")
    ApiResponse<BookDto> adjustStock(@RequestParam("maSach") String maSach, @RequestParam("delta") Integer delta);
}

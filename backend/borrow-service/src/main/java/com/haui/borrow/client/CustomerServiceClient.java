package com.haui.borrow.client;

import com.haui.borrow.client.dto.CustomerDto;
import com.haui.common.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "customer-service", url = "${services.customer-service.url:http://localhost:8083}")
public interface CustomerServiceClient {

    @GetMapping("/api/KhachHang/GetKhachHangById")
    ApiResponse<CustomerDto> getKhachHangById(@RequestParam("MaKH") String maKH);
}

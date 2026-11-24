package com.haui.customer.mapper;

import com.haui.customer.dto.request.KhachHangRequest;
import com.haui.customer.dto.response.KhachHangResponse;
import com.haui.customer.entity.KhachHang;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KhachHangMapper {
    KhachHang toEntity(KhachHangRequest request);
    KhachHangResponse toResponse(KhachHang entity);
    List<KhachHangResponse> toResponseList(List<KhachHang> entityList);

    @Mapping(target = "maKH", ignore = true)
    void updateEntityFromRequest(KhachHangRequest request, @MappingTarget KhachHang entity);
}

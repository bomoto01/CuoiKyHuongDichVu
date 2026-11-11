package com.haui.library.mapper;

import com.haui.library.dto.request.KhachHangRequest;
import com.haui.library.dto.response.KhachHangResponse;
import com.haui.library.entity.KhachHang;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KhachHangMapper {

    KhachHang toEntity(KhachHangRequest request);

    KhachHangResponse toResponse(KhachHang entity);

    List<KhachHangResponse> toResponseList(List<KhachHang> list);

    void updateEntityFromRequest(KhachHangRequest request, @MappingTarget KhachHang entity);
}

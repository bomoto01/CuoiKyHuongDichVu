package com.haui.library.mapper;

import com.haui.library.dto.request.TaiKhoanRequest;
import com.haui.library.dto.response.TaiKhoanResponse;
import com.haui.library.entity.TaiKhoan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaiKhoanMapper {

    @Mapping(target = "pass", ignore = true)
    TaiKhoan toEntity(TaiKhoanRequest request);

    TaiKhoanResponse toResponse(TaiKhoan entity);

    List<TaiKhoanResponse> toResponseList(List<TaiKhoan> list);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "pass", ignore = true)
    void updateEntityFromRequest(TaiKhoanRequest request, @MappingTarget TaiKhoan entity);
}

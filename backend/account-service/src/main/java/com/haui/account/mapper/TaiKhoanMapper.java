package com.haui.account.mapper;

import com.haui.account.dto.request.TaiKhoanRequest;
import com.haui.account.dto.response.TaiKhoanResponse;
import com.haui.account.entity.TaiKhoan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaiKhoanMapper {
    TaiKhoan toEntity(TaiKhoanRequest request);
    TaiKhoanResponse toResponse(TaiKhoan entity);
    List<TaiKhoanResponse> toResponseList(List<TaiKhoan> entityList);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "pass", ignore = true)
    void updateEntityFromRequest(TaiKhoanRequest request, @MappingTarget TaiKhoan entity);
}

package com.haui.library.mapper;

import com.haui.library.dto.request.TheLoaiRequest;
import com.haui.library.dto.response.TheLoaiResponse;
import com.haui.library.entity.TheLoai;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TheLoaiMapper {

    TheLoai toEntity(TheLoaiRequest request);

    @Mapping(target = "soLuongDauSach", ignore = true)
    TheLoaiResponse toResponse(TheLoai entity);

    List<TheLoaiResponse> toResponseList(List<TheLoai> list);

    void updateEntityFromRequest(TheLoaiRequest request, @MappingTarget TheLoai entity);
}

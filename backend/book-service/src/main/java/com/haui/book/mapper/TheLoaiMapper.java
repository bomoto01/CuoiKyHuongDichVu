package com.haui.book.mapper;

import com.haui.book.dto.request.TheLoaiRequest;
import com.haui.book.dto.response.TheLoaiResponse;
import com.haui.book.entity.TheLoai;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TheLoaiMapper {
    TheLoai toEntity(TheLoaiRequest request);
    TheLoaiResponse toResponse(TheLoai entity);
    List<TheLoaiResponse> toResponseList(List<TheLoai> entityList);

    @Mapping(target = "maTheLoai", ignore = true)
    void updateEntityFromRequest(TheLoaiRequest request, @MappingTarget TheLoai entity);
}

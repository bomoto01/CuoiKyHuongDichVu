package com.haui.book.mapper;

import com.haui.book.dto.request.SachRequest;
import com.haui.book.dto.response.SachResponse;
import com.haui.book.entity.Sach;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SachMapper {

    @Mapping(target = "theLoai", ignore = true)
    Sach toEntity(SachRequest request);

    @Mapping(target = "maTheLoai", source = "theLoai.maTheLoai")
    @Mapping(target = "tenTheLoai", source = "theLoai.tenTheLoai")
    SachResponse toResponse(Sach entity);

    List<SachResponse> toResponseList(List<Sach> entityList);

    @Mapping(target = "maSach", ignore = true)
    @Mapping(target = "theLoai", ignore = true)
    void updateEntityFromRequest(SachRequest request, @MappingTarget Sach entity);
}

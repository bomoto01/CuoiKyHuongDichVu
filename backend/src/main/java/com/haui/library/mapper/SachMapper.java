package com.haui.library.mapper;

import com.haui.library.dto.request.SachRequest;
import com.haui.library.dto.response.SachResponse;
import com.haui.library.entity.Sach;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SachMapper {

    @Mapping(target = "theLoai", ignore = true)
    Sach toEntity(SachRequest request);

    @Mapping(target = "maTheLoai", source = "theLoai.maTheLoai")
    @Mapping(target = "tenTheLoai", source = "theLoai.tenTheLoai")
    SachResponse toResponse(Sach entity);

    List<SachResponse> toResponseList(List<Sach> list);

    @Mapping(target = "theLoai", ignore = true)
    void updateEntityFromRequest(SachRequest request, @MappingTarget Sach entity);
}

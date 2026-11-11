package com.haui.library.mapper;

import com.haui.library.dto.response.LichSuGiaoDichResponse;
import com.haui.library.entity.LichSuGiaoDich;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LichSuGiaoDichMapper {

    @Mapping(target = "username", source = "taiKhoan.username")
    @Mapping(target = "tenTK", source = "taiKhoan.tenTK")
    @Mapping(target = "maKH", source = "khachHang.maKH")
    @Mapping(target = "tenKH", source = "khachHang.tenKH")
    @Mapping(target = "maSach", source = "sach.maSach")
    @Mapping(target = "tenSach", source = "sach.tenSach")
    LichSuGiaoDichResponse toResponse(LichSuGiaoDich entity);

    List<LichSuGiaoDichResponse> toResponseList(List<LichSuGiaoDich> list);
}

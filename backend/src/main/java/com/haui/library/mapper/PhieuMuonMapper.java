package com.haui.library.mapper;

import com.haui.library.dto.response.PhieuMuonResponse;
import com.haui.library.entity.PhieuMuon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhieuMuonMapper {

    @Mapping(target = "maKH", source = "khachHang.maKH")
    @Mapping(target = "tenKH", source = "khachHang.tenKH")
    @Mapping(target = "sdtKH", source = "khachHang.sdtKH")
    @Mapping(target = "maSach", source = "sach.maSach")
    @Mapping(target = "tenSach", source = "sach.tenSach")
    @Mapping(target = "tacGia", source = "sach.tacGia")
    @Mapping(target = "theLoai", source = "sach.theLoai.tenTheLoai")
    @Mapping(target = "daQuaHan", expression = "java(entity.getNgayTra() != null && entity.getNgayTra().isBefore(java.time.LocalDateTime.now()))")
    PhieuMuonResponse toResponse(PhieuMuon entity);

    List<PhieuMuonResponse> toResponseList(List<PhieuMuon> list);
}

package com.haui.common.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    MUON("Mượn sách"),
    TRA("Trả sách"),
    MAT("Mất sách"),
    HUY_SACH("Hủy sách");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }
}

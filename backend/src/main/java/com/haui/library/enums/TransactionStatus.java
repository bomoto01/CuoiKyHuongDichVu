package com.haui.library.enums;

public enum TransactionStatus {
    MUON("Mượn sách"),
    TRA("Trả sách"),
    MAT("Mất sách"),
    THEM_SACH("Thêm số lượng sách"),
    HUY_SACH("Hủy số lượng sách");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

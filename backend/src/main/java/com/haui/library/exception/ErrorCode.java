package com.haui.library.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định từ hệ thống", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Thông tin khóa không hợp lệ", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1002, "Chưa xác thực hoặc phiên đăng nhập hết hạn", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1003, "Bạn không có quyền thực hiện chức năng này", HttpStatus.FORBIDDEN),
    USER_NOT_EXISTED(1004, "Tài khoản không tồn tại trong hệ thống", HttpStatus.NOT_FOUND),
    USER_EXISTED(1005, "Tên tài khoản đã tồn tại", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(1006, "Tên đăng nhập hoặc mật khẩu không chính xác", HttpStatus.BAD_REQUEST),
    
    // Category (The Loai)
    CATEGORY_NOT_FOUND(2001, "Thể loại sách không tồn tại", HttpStatus.NOT_FOUND),
    CATEGORY_EXISTED(2002, "Mã thể loại đã tồn tại", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_BOOKS(2003, "Không thể xóa thể loại đang có chứa sách", HttpStatus.BAD_REQUEST),

    // Book (Sach)
    BOOK_NOT_FOUND(3001, "Sách không tồn tại trong hệ thống", HttpStatus.NOT_FOUND),
    BOOK_EXISTED(3002, "Mã sách đã tồn tại", HttpStatus.BAD_REQUEST),
    BOOK_INSUFFICIENT_STOCK(3003, "Số lượng sách trong kho không đủ", HttpStatus.BAD_REQUEST),
    BOOK_BEING_BORROWED(3004, "Không thể xóa sách đang ở trạng thái mượn", HttpStatus.BAD_REQUEST),
    INVALID_BOOK_QUANTITY(3005, "Số lượng sách phải lớn hơn 0", HttpStatus.BAD_REQUEST),

    // Customer (Khach Hang)
    CUSTOMER_NOT_FOUND(4001, "Khách hàng không tồn tại", HttpStatus.NOT_FOUND),
    CUSTOMER_EXISTED(4002, "Mã khách hàng đã tồn tại", HttpStatus.BAD_REQUEST),
    CUSTOMER_HAS_ACTIVE_BORROW(4003, "Khách hàng đang có đơn mượn sách, không thể xóa", HttpStatus.BAD_REQUEST),

    // Borrow (Phieu Muon)
    BORROW_NOT_FOUND(5001, "Phiếu mượn không tồn tại", HttpStatus.NOT_FOUND),
    BORROW_EXISTED(5002, "Mã phiếu mượn đã tồn tại", HttpStatus.BAD_REQUEST),
    INVALID_RETURN_QUANTITY(5003, "Số lượng trả lớn hơn số lượng đang mượn", HttpStatus.BAD_REQUEST),
    INVALID_LOST_QUANTITY(5004, "Số lượng mất vượt quá số lượng đang mượn", HttpStatus.BAD_REQUEST),
    INVALID_BORROW_DATE(5005, "Ngày trả phải sau ngày mượn", HttpStatus.BAD_REQUEST),

    // Transaction History
    TRANSACTION_NOT_FOUND(6001, "Không tìm thấy thông tin giao dịch", HttpStatus.NOT_FOUND);

    private final int code;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}


package com.ibizabroker.lms.dto;

import com.ibizabroker.lms.enums.ReturnReqStatus;

public class PendingReturnDTO {
    private Integer borrowId;
    private Integer userId;
    private String userName;
    private Integer bookId;
    private String bookName;
    private ReturnReqStatus returnRequestStatus;

    public PendingReturnDTO(Integer borrowId, Integer userId, String userName, Integer bookId, String bookName, ReturnReqStatus returnRequestStatus) {
        this.borrowId = borrowId;
        this.userId = userId;
        this.userName = userName;
        this.bookId = bookId;
        this.bookName = bookName;
        this.returnRequestStatus = returnRequestStatus;
    }

    public Integer getBorrowId() {
        return borrowId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public ReturnReqStatus getReturnRequestStatus() {
        return returnRequestStatus;
    }
}

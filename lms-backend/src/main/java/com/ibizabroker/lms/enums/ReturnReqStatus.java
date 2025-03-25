package com.ibizabroker.lms.enums;

/**
 * Enumeration representing the possible statuses of a book return request.
 * Used to track the state of a user's request to return a borrowed book.
 *
 * @author codematrix
 * @version 1.0
 */
public enum ReturnReqStatus {
    /** No return request has been made */
    NONE,
    
    /** Return request is waiting for approval */
    PENDING,
    
    /** Return request has been approved */
    APPROVED,
    
    /** Return request has been rejected */
    REJECTED
}


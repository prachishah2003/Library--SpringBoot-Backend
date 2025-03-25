package com.ibizabroker.lms.service;
import com.ibizabroker.lms.dao.RequestedBookRepository;
import com.ibizabroker.lms.dao.BorrowRepository;
import com.ibizabroker.lms.enums.ReturnReqStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

/**
 * Service class responsible for managing book requests in the library system.
 * Handles both new book requests and book return requests.
 * Uses constructor-based dependency injection through Lombok's @RequiredArgsConstructor.
 *
 * @author codematrix
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class RequestService {
    
    /**
     * Repository for managing new book requests.
     * Automatically injected through constructor due to final modifier.
     */
    private final RequestedBookRepository requestedBookRepository;

    /**
     * Repository for managing book borrow and return requests.
     * Automatically injected through constructor due to final modifier.
     */
    private final BorrowRepository borrowRepository;

    /**
     * Gets the total number of books that have been requested to be added to the library.
     *
     * @return Count of requested books
     */
    public long getTotalRequestedBooks() {
        return requestedBookRepository.count();
    }

    /**
     * Gets the total number of pending book return requests.
     * Only counts requests with PENDING status.
     *
     * @return Count of pending return requests
     */
    public long getTotalReturnRequests() {
        return borrowRepository.countByReturnRequestStatus(ReturnReqStatus.PENDING);
    }
}


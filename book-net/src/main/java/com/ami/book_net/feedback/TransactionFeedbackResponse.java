package com.ami.book_net.feedback;

public record TransactionFeedbackResponse(
        Double rating,
        String comment,
        Integer bookId,
        Integer borrowerId,
        Integer transactionId
) {}

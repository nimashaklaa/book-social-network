package com.ami.book_net.feedback;

import com.ami.book_net.book.Book;
import com.ami.book_net.book.BookRequest;
import com.ami.book_net.book.BookResponse;
import com.ami.book_net.book.BorrowedBookResponse;
import com.ami.book_net.file.FileUtils;
import com.ami.book_net.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest request){
        return Feedback.builder()
                .rating(request.rating())
                .comment(request.comment())
                .book(Book.builder().id(request.bookId()).build())
                .build();
    }
}

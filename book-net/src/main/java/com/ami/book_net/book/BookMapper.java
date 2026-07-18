package com.ami.book_net.book;

import com.ami.book_net.feedback.Feedback;
import com.ami.book_net.feedback.FeedbackRepository;
import com.ami.book_net.file.FileUtils;
import com.ami.book_net.history.BookTransactionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookMapper {

    private final FeedbackRepository feedbackRepository;



    public Book toBook(BookRequest request){
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .isbn(request.isbn())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .owner(book.getOwner().fullName())
                .cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
        Optional<Feedback> feedbackOptional = feedbackRepository.findBySpecificTransactionFeedback(
                history.getBook().getId(),
                history.getUser().getId(),
                history.getId()
        );
        return BorrowedBookResponse.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .rate(feedbackOptional.map(Feedback::getRating).orElse(0.0))
                .comment(feedbackOptional.map(Feedback::getComment).orElse(null))
                .borrower(history.getUser().getEmail())
                .borrowedDate(history.getCreatedDate())
                .returnedDate(history.getReturnDate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}

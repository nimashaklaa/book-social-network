package com.ami.book_net.feedback;

import com.ami.book_net.book.Book;
import com.ami.book_net.book.BookRepository;
import com.ami.book_net.exception.OperationNotPermittedException;
import com.ami.book_net.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    public Integer save(@Valid FeedbackRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(request.bookId()).orElseThrow(()-> new IllegalArgumentException("Book not found"));
        if(book.isArchived()|| !book.isShareable()){
            throw new OperationNotPermittedException("You cannot give feedback to this book because it is not available");
        }
        if(book.getOwner().getId().equals(user.getId())){
            throw new OperationNotPermittedException("You cannot give feedback to your own book");
        }
        Feedback feedback = feedbackMapper.toFeedback(request);
        return feedbackRepository.save(feedback).getId();
    }
}

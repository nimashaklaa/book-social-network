package com.ami.book_net.feedback;

import com.ami.book_net.book.Book;
import com.ami.book_net.book.BookRepository;
import com.ami.book_net.book.PageResponse;
import com.ami.book_net.exception.OperationNotPermittedException;
import com.ami.book_net.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PageResponse<FeedbackResponse> findAllFeedbackByBook(Integer bookId, int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Feedback> feedback = feedbackRepository.findAllByBookId(pageable, bookId);
        List<FeedbackResponse> feedbackResponse = feedback.stream()
                .map(f-> feedbackMapper.toFeedbackResponse(f,user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponse,
                feedback.getNumber(),
                feedback.getSize(),
                feedback.getTotalPages(),
                feedback.getTotalElements(),
                feedback.isFirst(),
                feedback.isLast()
        );
    }
    public TransactionFeedbackResponse getReviewForTransaction(Integer bookId, Integer userId, Integer historyId) {
        return feedbackRepository.findBySpecificTransactionFeedback(bookId, userId, historyId)
                .map(f -> new TransactionFeedbackResponse(
                        f.getRating(),
                        f.getComment(),
                        f.getBook().getId(),
                        f.getHistory().getUser().getId(),
                        f.getHistory().getId()
                ))
                .orElseThrow(() -> new EntityNotFoundException("No review found for this specific transaction"));
    }
}

package com.ami.book_net.book;

import com.ami.book_net.exception.OperationNotPermittedException;
import com.ami.book_net.history.BookTransactionHistory;
import com.ami.book_net.history.BookTransactionHistoryRepository;
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
import java.util.Objects;

import static com.ami.book_net.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;

    public Integer save(@Valid BookRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse findBookById(Integer bookId) {
        return bookMapper.toBookResponse(findBookByIdHelper(bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return toPageResponse(books, bookResponse);
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()), pageable);
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return toPageResponse(books, bookResponse);
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBook(pageable, user.getId());
        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return toPageResponse(allBorrowedBooks, bookResponse);
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllReturnedBook(pageable, user.getId());
        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return toPageResponse(allBorrowedBooks, bookResponse);
    }

    public Integer updateBookShareableStatus(Integer bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = findBookByIdHelper(bookId);
        validateUserIsNotOwner(book, user, "You cannot update others books shareable status");
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateBookArchivedStatus(Integer bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = findBookByIdHelper(bookId);
        validateUserIsNotOwner(book, user, "You cannot update others books archived status");
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = findBookByIdHelper(bookId);
        if(!book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You cannot borrow this book because it is not available");
        }
        validateUserIsOwner(book, user);
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowed(bookId, user.getId());
        if(isAlreadyBorrowed){
            throw new OperationNotPermittedException("You cannot borrow this book because you already borrowed it");
        }
        final boolean isCurrentlyBorrowed = bookTransactionHistoryRepository.isCurrentlyBorrowed(bookId);
        if(isCurrentlyBorrowed){
            throw new OperationNotPermittedException("You cannot borrow this book because someone already borrowed it");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBorrowBook(Integer bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = findBookByIdHelper(bookId);
        if(!book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You cannot borrow this book because it is not available");
        }
        validateUserIsOwner(book, user);
        BookTransactionHistory borrowedHistory = bookTransactionHistoryRepository.findBookByUserIdAndBookId(bookId, user.getId()).orElseThrow(()-> new OperationNotPermittedException("You cannot return this book because you did not borrow it"));
        borrowedHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(borrowedHistory).getId();
    }
    public Integer approveReturnBook(Integer bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = findBookByIdHelper(bookId);
        if(!book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You cannot borrow this book because it is not available");
        }
        validateUserIsOwner(book, user);
        BookTransactionHistory borrowedHistory = bookTransactionHistoryRepository.findBookByOwnerIdAndBookId(bookId, user.getId()).orElseThrow(()-> new OperationNotPermittedException("You cannot approve this book return because you did not borrow it"));
        borrowedHistory.setReturnApproved(true);
        return bookTransactionHistoryRepository.save(borrowedHistory).getId();
    }

    // --- REUSABLE PRIVATE HELPER METHODS ---

    private Book findBookByIdHelper(Integer bookId){
        return bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("No book found with the Id"+bookId));
    }
    private <S,T> PageResponse<T> toPageResponse(Page<S> pageMetaData, List<T> content){
        return new PageResponse<>(
                content,
                pageMetaData.getNumber(),
                pageMetaData.getSize(),
                pageMetaData.getTotalPages(),
                pageMetaData.getTotalElements(),
                pageMetaData.isFirst(),
                pageMetaData.isLast()
        );
    }
    private void validateUserIsOwner(Book book, User user) {
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }
    }
    private void validateUserIsNotOwner(Book book, User user, String exceptionMessage) {
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException(exceptionMessage);
        }
    }


}

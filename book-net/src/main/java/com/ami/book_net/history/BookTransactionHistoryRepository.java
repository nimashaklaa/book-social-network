package com.ami.book_net.history;

import com.ami.book_net.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {
    @Query("""
            SELECT history FROM BookTransactionHistory history WHERE history.user.id =:userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBook(Pageable pageable, Integer userId);
}

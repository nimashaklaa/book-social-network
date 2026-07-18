package com.ami.book_net.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer>, JpaSpecificationExecutor<Feedback> {
    @Query("""
    SELECT feedback
    FROM Feedback feedback
    WHERE feedback.book.id = :id
    """)
    Page<Feedback> findAllByBookId(Pageable pageable, Integer id);


    @Query("""
         SELECT f FROM Feedback f
                    WHERE f.book.id = :bookId
                    AND f.history.user.id = :userId
                    AND f.history.id = :historyId
        """)
    Optional<Feedback> findBySpecificTransactionFeedback(
            @Param("bookId") Integer bookId,
            @Param("userId") Integer userId,
            @Param("historyId") Integer historyId
    );
}

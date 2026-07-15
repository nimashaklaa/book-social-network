package com.ami.book_net.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer>, JpaSpecificationExecutor<Feedback> {
    @Query("""
    SELECT feedback
    FROM Feedback feedback
    WHERE feedback.book.id = :id
    """)
    Page<Feedback> findAllByBookId(Pageable pageable, Integer id);
}

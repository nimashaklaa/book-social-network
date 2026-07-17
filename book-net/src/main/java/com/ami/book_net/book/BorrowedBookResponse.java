package com.ami.book_net.book;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {
    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private double rate;
    private String borrower;
    private boolean returned;
    private boolean returnApproved;
    private LocalDateTime borrowedDate;
    private LocalDateTime returnedDate;
}

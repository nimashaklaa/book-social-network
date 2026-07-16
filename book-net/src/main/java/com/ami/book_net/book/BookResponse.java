package com.ami.book_net.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private byte[] cover;
    private double rate;
    private boolean archived;
    private boolean shareable;
    private boolean borrowed;
    private String owner;
}

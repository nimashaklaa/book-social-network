package com.ami.book_net.book;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequest (
        Integer id,
        @NotNull( message = "Title is required")
        @NotEmpty( message = "Title is required")
        String title,
        @NotNull( message = "Author name is required")
        @NotEmpty( message = "Author name is required")
        String authorName,
        @NotNull( message = "ISBN is required")
        @NotEmpty( message = "ISBN is required")
        String isbn,
        @NotNull( message = "Synopsis is required")
        @NotEmpty( message = "Synopsis is required")
        String synopsis,
        @NotNull( message = "Shareable status is required")
        boolean shareable
){}

package com.ami.book_net.feedback;


import jakarta.validation.constraints.*;

public record FeedbackRequest (
        @Positive(message = "Rating must be positive")
        @NotNull(message = "Rating is required")
        @Max(value = 5, message = "Rating must be between 1 and 5")
        @Min(value = 1, message = "Rating must be between 1 and 5")
        Double rating,

        @NotNull(message = "Comment is required")
        @NotBlank(message = "Comment is required")
        String comment,

        @NotNull(message = "Book id is required")
        Integer bookId
){}

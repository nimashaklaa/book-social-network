package com.ami.book_net.feedback;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponse {
    private Double rating;
    private String comment;
    private boolean ownFeedback;
}

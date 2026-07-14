package com.ami.book_net.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    //Method 2
    public static Specification<Book> withOwnerId(Integer ownerId){
        return (root, query, builder) -> builder.equal(root.get("owner").get("id"), ownerId);
    }
}

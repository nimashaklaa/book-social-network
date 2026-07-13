package com.ami.book_net.email;

import lombok.Getter;

@Getter // Adding this tells IntelliJ the field is accessed globally!
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account");
    // Here Java creates a public static final instance of itself!
    // public static final EmailTemplateName ACTIVATE_ACCOUNT = new EmailTemplateName("activate_account");

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}

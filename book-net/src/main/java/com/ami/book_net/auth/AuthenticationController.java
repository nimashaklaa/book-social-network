package com.ami.book_net.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController //this class is an HTTP endpoint provider
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication") // Swagger / OpenAPI annotation
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
            // @Valid -> This triggers your Jakarta/Hibernate Validator engine.
    ) {
        service.register(request);
        return ResponseEntity.accepted().build();
    }

}

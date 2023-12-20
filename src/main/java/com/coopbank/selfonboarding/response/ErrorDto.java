package com.coopbank.selfonboarding.response;

import java.util.Set;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDto {
    private String error;
    private String message;
    private int status;

    public ErrorDto(String error, String message, int status) {
        this.error = error;
        this.message = message;
        this.status = status;
    }

    // Getters and setters
}

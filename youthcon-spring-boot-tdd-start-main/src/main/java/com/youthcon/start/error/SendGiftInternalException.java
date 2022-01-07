package com.youthcon.start.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SendGiftInternalException extends RuntimeException {
    public SendGiftInternalException(String message) {
        super(message);
    }
}
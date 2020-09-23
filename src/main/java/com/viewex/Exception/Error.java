package com.viewex.Exception;

public class Error extends RuntimeException{
    public Error(String message) {
        super(message);
    }

    public Error(String message, Throwable cause) {
        super(message, cause);
    }
}

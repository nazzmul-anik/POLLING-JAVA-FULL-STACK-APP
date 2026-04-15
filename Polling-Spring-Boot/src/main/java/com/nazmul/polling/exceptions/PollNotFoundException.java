package com.nazmul.polling.exceptions;

public class PollNotFoundException extends RuntimeException{
    public PollNotFoundException(String message){
        super(message);
    }
}

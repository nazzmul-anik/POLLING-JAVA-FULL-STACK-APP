package com.nazmul.polling.exceptions;

import com.nazmul.polling.entity.User;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String message){
        super(message);
    }
}

package com.backend.portalroshkabackend.tools.errors.errorslist;

public class DatabaseOperationException extends RuntimeException{
    public DatabaseOperationException(String message){
        super(message);
    }

    public DatabaseOperationException(String message, Throwable cause){
        super(message, cause);
    }
}

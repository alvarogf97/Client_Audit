package com.example.alvaro.client_audit.core.exceptions;

public class ConnectionException extends RuntimeException {

    public ConnectionException(){
        super();
    }

    public ConnectionException(String msg){
        super(msg);
    }
}

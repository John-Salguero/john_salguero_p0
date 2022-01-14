package com.johnsbank.java.utilities;

public class ResourceNotFoundException extends Exception {
    ResourceNotFoundException() {super();}
    ResourceNotFoundException(String msg) { super(msg); }
    ResourceNotFoundException(String msg, Throwable cause) { super(msg, cause); }
    ResourceNotFoundException(Throwable cause) {super(cause);}
    ResourceNotFoundException(String message, Throwable cause,
              boolean enableSuppression,
              boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace);}

}

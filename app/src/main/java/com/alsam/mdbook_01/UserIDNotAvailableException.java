package com.alsam.mdbook_01;

public class UserIDNotAvailableException extends Exception {
    /**
     * Throws error without error message.
     */
    public UserIDNotAvailableException(){
        super();
    }

    /**
     * @param errorMessage An error message to provide extra information.
     */
    public UserIDNotAvailableException(String errorMessage){
        super(errorMessage);
    }
}

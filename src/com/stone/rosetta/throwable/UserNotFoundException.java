package com.stone.rosetta.throwable;

public class UserNotFoundException extends Throwable {

    public UserNotFoundException(String s, String userName) {
        super(String.format(s, userName));
    }
}

package com.stone.rosetta.throwable;

public class EntityNotUpdatedException extends Throwable{

    public EntityNotUpdatedException(String message, Object... objs) {
        super(String.format(message, objs));
    }
}

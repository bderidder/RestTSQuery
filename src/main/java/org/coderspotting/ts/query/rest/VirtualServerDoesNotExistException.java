package org.coderspotting.ts.query.rest;

public class VirtualServerDoesNotExistException extends Exception
{
    public VirtualServerDoesNotExistException(String message)
    {
        super(message);
    }
}

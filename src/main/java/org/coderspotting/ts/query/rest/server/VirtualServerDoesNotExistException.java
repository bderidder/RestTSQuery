package org.coderspotting.ts.query.rest.server;

public class VirtualServerDoesNotExistException extends Exception
{
    public VirtualServerDoesNotExistException(String message)
    {
        super(message);
    }
}

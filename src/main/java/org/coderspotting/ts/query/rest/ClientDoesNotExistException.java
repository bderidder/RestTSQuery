package org.coderspotting.ts.query.rest;

public class ClientDoesNotExistException extends Exception
{
    public ClientDoesNotExistException(String message)
    {
        super(message);
    }
}

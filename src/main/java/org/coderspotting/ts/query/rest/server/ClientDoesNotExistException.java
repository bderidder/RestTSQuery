package org.coderspotting.ts.query.rest.server;

public class ClientDoesNotExistException extends Exception
{
    public ClientDoesNotExistException(String message)
    {
        super(message);
    }
}

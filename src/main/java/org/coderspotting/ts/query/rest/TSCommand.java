package org.coderspotting.ts.query.rest;

public class TSCommand
{  
    private String rawCommand;
    
    public TSCommand(String rawCommand)
    {
        this.rawCommand = rawCommand;
    }
    
    public String getRawCommand()
    {
        return rawCommand;
    }
}

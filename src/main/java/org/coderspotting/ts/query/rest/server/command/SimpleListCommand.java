package org.coderspotting.ts.query.rest.server.command;

import java.util.HashMap;
import java.util.List;

public class SimpleListCommand implements IListCommand
{
    private int listType;
    private List<HashMap<String, String>> rawOutput;

    public SimpleListCommand(int listType)
    {
        this.listType = listType;
    }

    @Override
    public int getListMode()
    {
        return this.listType;
    }
    
    @Override
    public String getParameters()
    {
        return null;
    }

    @Override
    public void processRawOutput(List<HashMap<String, String>> rawOutput)
    {
        this.rawOutput = rawOutput;
    }

    public List<HashMap<String, String>> getRawOutput()
    {
        return this.rawOutput;
    }
}

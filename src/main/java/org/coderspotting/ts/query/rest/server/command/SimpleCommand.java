package org.coderspotting.ts.query.rest.server.command;

import java.util.HashMap;
import java.util.List;

public class SimpleCommand implements ICommand
{
    private String nativeTSCommand;
    private List<HashMap<String, String>> rawOutput;

    public SimpleCommand(String nativeTSCommand)
    {
        this.nativeTSCommand = nativeTSCommand;
    }

    @Override
    public String getNativeTSCommand()
    {
        return this.nativeTSCommand;
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

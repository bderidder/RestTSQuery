package org.coderspotting.ts.query.rest.server.command;

import java.util.HashMap;
import java.util.List;

public interface ICommand
{
    String getNativeTSCommand();

    void processRawOutput(List<HashMap<String, String>> rawOutput);
}

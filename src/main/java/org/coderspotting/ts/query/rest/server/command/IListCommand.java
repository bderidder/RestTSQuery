package org.coderspotting.ts.query.rest.server.command;

import java.util.HashMap;
import java.util.List;

public interface IListCommand
{
    int getListMode();
    String getParameters();

    void processRawOutput(List<HashMap<String, String>> rawOutput);
}

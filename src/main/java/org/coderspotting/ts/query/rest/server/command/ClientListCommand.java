package org.coderspotting.ts.query.rest.server.command;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class ClientListCommand extends SimpleListCommand
{
    public ClientListCommand()
    {
        super(JTS3ServerQuery.LISTMODE_CLIENTLIST);
    }
}

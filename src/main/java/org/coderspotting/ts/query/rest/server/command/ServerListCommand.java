package org.coderspotting.ts.query.rest.server.command;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class ServerListCommand extends SimpleListCommand
{
    public ServerListCommand()
    {
        super(JTS3ServerQuery.LISTMODE_SERVERLIST);
    }
}

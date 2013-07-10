package org.coderspotting.ts.query.rest.server.command;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class ChannelListCommand extends SimpleListCommand
{
    public ChannelListCommand()
    {
        super(JTS3ServerQuery.LISTMODE_CHANNELLIST);
    }
}

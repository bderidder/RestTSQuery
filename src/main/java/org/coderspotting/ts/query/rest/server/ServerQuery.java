package org.coderspotting.ts.query.rest.server;

import org.coderspotting.ts.query.rest.server.command.SimpleCommand;
import de.stefan1200.jts3serverquery.JTS3ServerQuery;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.coderspotting.ts.query.cache.CacheFactory;
import org.coderspotting.ts.query.cache.CacheTime;
import org.coderspotting.ts.query.config.Configuration;
import org.coderspotting.ts.query.config.ConfigurationFactory;
import org.coderspotting.ts.query.rest.server.command.ICommand;

public class ServerQuery
{
    private static String CACHE_SERVERS_KEY = "Servers.Cache";
    private static String CACHE_CLIENTS_KEY = "Clients.Cache";
    private static String CACHE_CHANNELS_KEY = "Channels.Cache";
    private static long CACHE_TTL = 60 * CacheTime.SECOND;

    public ServerQuery()
    {
    }

    public List<HashMap<String, String>> getServerList() throws
            CouldNotConnectException, VirtualServerDoesNotExistException,
            CouldNotGetListException
    {
        return getListFromCache(CACHE_SERVERS_KEY,
                JTS3ServerQuery.LISTMODE_SERVERLIST, -1);
    }

    public List<HashMap<String, String>> getClientList(int virtualServer) throws
            CouldNotConnectException, VirtualServerDoesNotExistException,
            CouldNotGetListException
    {
        return scrubLocalUsers(getListFromCache(CACHE_CLIENTS_KEY,
                JTS3ServerQuery.LISTMODE_CLIENTLIST, virtualServer));
    }

    public List<HashMap<String, String>> getChannelList(int virtualServer)
            throws CouldNotConnectException, VirtualServerDoesNotExistException,
            CouldNotGetListException
    {
        return getListFromCache(CACHE_CHANNELS_KEY,
                JTS3ServerQuery.LISTMODE_CHANNELLIST, virtualServer);
    }

    public void doCommand(ICommand command, int virtualServer)
            throws CouldNotConnectException, VirtualServerDoesNotExistException,
            CouldNotExecuteCommandException
    {
        getCommandFromCache(command, virtualServer);
    }

    private void getCommandFromCache(ICommand command, int virtualServer) throws
            CouldNotConnectException, VirtualServerDoesNotExistException, CouldNotExecuteCommandException
    {
        String cacheKey = "Commands.Cache." + command.getNativeTSCommand();

        List<HashMap<String, String>> commandData = (List<HashMap<String, String>>) CacheFactory.getCache().getEntry(cacheKey);

        // do we have a cached version?
        if (commandData == null)
        {
            commandData = executeCommand(command, virtualServer);

            CacheFactory.getCache().putEntry(cacheKey, commandData, CACHE_TTL);
        }

        command.processRawOutput(commandData);
    }

    private List<HashMap<String, String>> getListFromCache(String cacheKey,
            int listType, int virtualServer) throws CouldNotConnectException,
            VirtualServerDoesNotExistException, CouldNotGetListException
    {
        List<HashMap<String, String>> dataList = (List<HashMap<String, String>>) CacheFactory.
                getCache().getEntry(cacheKey);

        // do we have a cached version?
        if (dataList == null)
        {
            dataList = getList(listType, virtualServer);

            CacheFactory.getCache().putEntry(cacheKey, dataList, CACHE_TTL);
        }

        return dataList;
    }

    private List<HashMap<String, String>> executeCommand(ICommand command,
            int virtualServer) throws CouldNotConnectException,
            VirtualServerDoesNotExistException, CouldNotExecuteCommandException
    {
        Configuration config = ConfigurationFactory.getConfiguration();

        JTS3ServerQuery query = null;

        try
        {
            query = new JTS3ServerQuery();

            if (!query.connectTS3Query(config.getProperty("Teamspeak.Server.Host", "localhost"), Integer.parseInt(
                    config.getProperty("Teamspeak.Server.Port", "10011"))))
            {
                echoError(query);

                throw new CouldNotConnectException(
                        "Could not connect to TeamSpeak server");
            }

            query.loginTS3(config.getProperty("Teamspeak.Login.Username"), config.
                    getProperty("Teamspeak.Login.Password"));

            if ((virtualServer != -1) && !query.selectVirtualServer(
                    virtualServer))
            {
                echoError(query);

                throw new VirtualServerDoesNotExistException(
                        "Virtual server does not exist: " + virtualServer);
            }

            HashMap<String, String> rawOutput = query.doCommand(command.getNativeTSCommand());

            if (rawOutput != null)
            {
                String outputId = rawOutput.get("id");

                if ("0".equals(outputId))
                {
                    String response = rawOutput.get("response");

                    List<HashMap<String, String>> output = query.parseRawData(response);

                    return output;
                }
                else
                {
                    echoError(query);

                    throw new CouldNotExecuteCommandException("Could not execute command " + command.getNativeTSCommand());
                }
            }
            else
            {
                echoError(query);

                throw new CouldNotExecuteCommandException("Could not execute command " + command.getNativeTSCommand());
            }
        }
        finally
        {
            if (query != null)
            {
                query.closeTS3Connection();
            }
        }
    }

    private List<HashMap<String, String>> getList(int listType,
            int virtualServer) throws CouldNotConnectException,
            VirtualServerDoesNotExistException, CouldNotGetListException
    {
        Configuration config = ConfigurationFactory.getConfiguration();

        JTS3ServerQuery query = null;

        try
        {
            query = new JTS3ServerQuery();

            if (!query.connectTS3Query(config.getProperty("Teamspeak.Server.Host", "localhost"), Integer.parseInt(
                    config.getProperty("Teamspeak.Server.Port", "10011"))))
            {
                echoError(query);

                throw new CouldNotConnectException(
                        "Could not connect to TeamSpeak server");
            }

            query.loginTS3(config.getProperty("Teamspeak.Login.Username"), config.
                    getProperty("Teamspeak.Login.Password"));

            if ((virtualServer != -1) && !query.selectVirtualServer(
                    virtualServer))
            {
                echoError(query);

                throw new VirtualServerDoesNotExistException(
                        "Virtual server does not exist: " + virtualServer);
            }

            List<HashMap<String, String>> dataChannelList = query.getList(listType);

            if (dataChannelList != null)
            {
                return dataChannelList;
            }
            else
            {
                echoError(query);

                throw new CouldNotGetListException("Could not fetch list");
            }
        }
        finally
        {
            if (query != null)
            {
                query.closeTS3Connection();
            }
        }
    }

    private List<HashMap<String, String>> scrubLocalUsers(
            List<HashMap<String, String>> clientList)
    {
        Iterator<HashMap<String, String>> it = clientList.iterator();

        while (it.hasNext())
        {
            HashMap<String, String> clientHashMap = it.next();

            if (clientHashMap.get("client_nickname").contains("from 127.0.0.1"))
            {
                it.remove();
            }
        }

        return clientList;
    }

    /*
     * Just output the last error message (if any)
     */
    void echoError(JTS3ServerQuery query)
    {
        String error = query.getLastError();

        if (error != null)
        {
            Logger.getLogger(ServerQuery.class.getName()).log(Level.SEVERE,
                    error);

            if (query.getLastErrorPermissionID() != -1)
            {
                HashMap<String, String> permInfo = query.getPermissionInfo(
                        query.getLastErrorPermissionID());

                if (permInfo != null)
                {
                    Logger.getLogger(ServerQuery.class.getName()).log(
                            Level.SEVERE, "Missing Permission: {0}", permInfo.
                            get("permname"));
                }
            }
        }
    }

    /*
     * Just output all key / value pairs
     */

}

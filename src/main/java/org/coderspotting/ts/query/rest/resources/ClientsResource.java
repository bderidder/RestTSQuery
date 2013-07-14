package org.coderspotting.ts.query.rest.resources;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.coderspotting.ts.query.rest.server.ClientDoesNotExistException;
import org.coderspotting.ts.query.rest.server.CouldNotConnectException;
import org.coderspotting.ts.query.rest.server.CouldNotGetListException;
import org.coderspotting.ts.query.rest.server.ServerQuery;
import org.coderspotting.ts.query.rest.server.VirtualServerDoesNotExistException;
import org.coderspotting.ts.query.rest.server.command.ClientListCommand;

@Path("/virtualservers/{serverId}/clients")
public class ClientsResource
{
    @Context
    private UriInfo context;

    public ClientsResource()
    {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients(@PathParam("serverId") int virtualServer)
    {
        try
        {
            ServerQuery query = new ServerQuery();

            ClientListCommand cmd = new ClientListCommand();

            query.doListCommand(cmd, virtualServer);

            List<HashMap<String, String>> clients = scrubLocalUsers(cmd.getRawOutput());

            JsonArrayBuilder arrBuilder = JsonHelper.hashMapListToJson(clients);

            String jsonData = JsonHelper.buildJsonData(arrBuilder);

            return Response.ok(jsonData, MediaType.APPLICATION_JSON).build();

        }
        catch (VirtualServerDoesNotExistException ex)
        {
            Logger.getLogger(ClientsResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.NOT_FOUND).entity(
                    "404 NOT FOUND - Virtual Server " + virtualServer).build();
        }
        catch (CouldNotGetListException ex)
        {
            Logger.getLogger(ClientsResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Could not get list").build();
        }
        catch (CouldNotConnectException ex)
        {
            Logger.getLogger(ClientsResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Could not connect to server").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{clientId}")
    public Response getClient(@PathParam("serverId") int virtualServer,
            @PathParam("clientId") int clientId)
    {
        String strCliendId = Integer.toString(clientId);

        try
        {
            ServerQuery query = new ServerQuery();

            ClientListCommand cmd = new ClientListCommand();

            query.doListCommand(cmd, virtualServer);

            List<HashMap<String, String>> clientList = scrubLocalUsers(cmd.getRawOutput());

            HashMap<String, String> clientHashMap = null;

            for (HashMap<String, String> hashMap : clientList)
            {
                String id = hashMap.get("client_database_id");

                if (strCliendId.equals(id))
                {
                    clientHashMap = hashMap;
                }
            }

            if (clientHashMap == null)
            {
                throw new ClientDoesNotExistException(
                        "Client does not exist: " + virtualServer);
            }

            JsonObjectBuilder objBuilder = JsonHelper.hashMapToJson(clientHashMap);

            String jsonData = JsonHelper.buildJsonData(objBuilder);

            return Response.ok(jsonData, MediaType.APPLICATION_JSON).build();
        }
        catch (VirtualServerDoesNotExistException ex)
        {
            Logger.getLogger(ClientsResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.NOT_FOUND).entity("404 NOT FOUND - Virtual Server " + virtualServer).
                    build();
        }
        catch (ClientDoesNotExistException ex)
        {
            Logger.getLogger(ClientsResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.NOT_FOUND).entity("404 NOT FOUND - Client " + clientId).build();
        }
        catch (CouldNotGetListException ex)
        {
            Logger.getLogger(ClientsResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Could not get list").build();
        }
        catch (CouldNotConnectException ex)
        {
            Logger.getLogger(ClientsResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Could not connect to server").build();
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
}

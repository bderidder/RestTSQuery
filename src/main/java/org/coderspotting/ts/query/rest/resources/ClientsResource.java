package org.coderspotting.ts.query.rest.resources;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.coderspotting.ts.query.rest.ClientDoesNotExistException;
import org.coderspotting.ts.query.rest.CouldNotConnectException;
import org.coderspotting.ts.query.rest.CouldNotExecuteCommandException;
import org.coderspotting.ts.query.rest.CouldNotGetListException;
import org.coderspotting.ts.query.rest.HashMapUtils;
import org.coderspotting.ts.query.rest.JsonHelper;
import org.coderspotting.ts.query.rest.ServerQuery;
import org.coderspotting.ts.query.rest.TSCommand;
import org.coderspotting.ts.query.rest.VirtualServerDoesNotExistException;

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

            List<HashMap<String, String>> clients = query.getClientList(virtualServer);

            List<HashMap<String, String>> cmdOutput = query.doCommand(new TSCommand("clientdblist"), virtualServer);
            
            HashMapUtils.outputHashMap(cmdOutput);
            
            try
            {
                String json = JsonHelper.hashMapListToJson(clients);

                return Response.ok(json, MediaType.APPLICATION_JSON).build();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ClientsResource.class.getName()).log(Level.SEVERE, null, ex);

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
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
        catch (CouldNotExecuteCommandException ex)
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

            List<HashMap<String, String>> clientList = query.getClientList(
                    virtualServer);

            try
            {
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

                JsonFactory jsonFactory = new JsonFactory();
                StringWriter strWriter = new StringWriter();

                try (JsonGenerator jsonGenerator = jsonFactory.
                        createJsonGenerator(strWriter))
                {
                    jsonGenerator.useDefaultPrettyPrinter();

                    jsonGenerator.writeStartObject();

                    JsonHelper.hashMapToJson(jsonGenerator, clientHashMap);

                    jsonGenerator.writeEndObject();

                    jsonGenerator.close();

                    return Response.ok(strWriter.toString(),
                            MediaType.APPLICATION_JSON).build();
                }
            }
            catch (IOException ex)
            {
                Logger.getLogger(VirtualServersResource.class.getName()).log(Level.SEVERE, null, ex);

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
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
}

package org.coderspotting.ts.query.rest.resources;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
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
import org.coderspotting.ts.query.rest.server.ServerQuery;
import org.coderspotting.ts.query.rest.server.VirtualServerDoesNotExistException;
import org.coderspotting.ts.query.rest.server.command.ServerListCommand;

@Path("/virtualservers")
public class VirtualServersResource
{
    @Context
    private UriInfo context;

    public VirtualServersResource()
    {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVirtualServers()
    {
        try
        {
            ServerQuery query = new ServerQuery();

            ServerListCommand cmd = new ServerListCommand();

            query.doListCommand(cmd, -1);

            List<HashMap<String, String>> serverList = cmd.getRawOutput();

            JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

            for (HashMap<String, String> hashMap : serverList)
            {
                String serverId = hashMap.get("virtualserver_id");
                arrBuilder.add(context.getBaseUri() + "virtualservers/" + serverId);
            }

            String jsonData = JsonHelper.buildJsonData(arrBuilder);

            return Response.ok(jsonData, MediaType.APPLICATION_JSON).build();
        }
        catch (VirtualServerDoesNotExistException ex)
        {
            Logger.getLogger(VirtualServersResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.NOT_FOUND).entity("404 NOT FOUND").build();
        }
        catch (Exception ex)
        {
            Logger.getLogger(VirtualServersResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{serverId}")
    public Response getVirtualServerDetail(@PathParam("serverId") int virtualServer)
    {
        String strVirtualServerId = Integer.toString(virtualServer);

        try
        {
            ServerQuery query = new ServerQuery();

            ServerListCommand cmd = new ServerListCommand();

            query.doListCommand(cmd, -1);

            List<HashMap<String, String>> serverList = cmd.getRawOutput();

            HashMap<String, String> serverHashMap = null;

            for (HashMap<String, String> hashMap : serverList)
            {
                String serverId = hashMap.get("virtualserver_id");

                if (strVirtualServerId.equals(serverId))
                {
                    serverHashMap = hashMap;
                }
            }

            if (serverHashMap == null)
            {
                throw new VirtualServerDoesNotExistException("Virtual server does not exist: " + virtualServer);
            }

            JsonObjectBuilder objBuilder = JsonHelper.hashMapToJson(serverHashMap);

            objBuilder.add("getChannels",
                    context.getBaseUri() + "virtualservers/" + strVirtualServerId + "/channels");
            objBuilder.add("getClients", context.getBaseUri() + "virtualservers/" + strVirtualServerId + "/clients");

            String jsonData = JsonHelper.buildJsonData(objBuilder);

            return Response.ok(jsonData, MediaType.APPLICATION_JSON).build();
        }
        catch (VirtualServerDoesNotExistException ex)
        {
            Logger.getLogger(VirtualServersResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.NOT_FOUND).entity("404 NOT FOUND").build();
        }
        catch (Exception ex)
        {
            Logger.getLogger(VirtualServersResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                    "Could not connect to Team Speak server").build();
        }
    }
}

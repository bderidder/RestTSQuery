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
import org.coderspotting.ts.query.rest.JsonHelper;
import org.coderspotting.ts.query.rest.ServerQuery;
import org.coderspotting.ts.query.rest.VirtualServerDoesNotExistException;

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

            List<HashMap<String, String>> serverList = query.getServerList();

            try
            {
                JsonFactory jsonFactory = new JsonFactory();
                StringWriter strWriter = new StringWriter();
                
                try (JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(strWriter))
                {
                    jsonGenerator.useDefaultPrettyPrinter();

                    jsonGenerator.writeStartArray();

                    for (HashMap<String, String> hashMap : serverList)
                    {
                        String serverId = hashMap.get("virtualserver_id");
                        jsonGenerator.writeObject(context.getBaseUri() + "virtualservers/" + serverId);
                    }

                    jsonGenerator.writeEndArray();
                }

                return Response.ok(strWriter.toString(), MediaType.APPLICATION_JSON).build();
            }
            catch (IOException ex)
            {
                Logger.getLogger(VirtualServersResource.class.getName()).log(Level.SEVERE, null, ex);

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
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

            List<HashMap<String, String>> serverList = query.getServerList();

            try
            {
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
                
                JsonFactory jsonFactory = new JsonFactory();
                StringWriter strWriter = new StringWriter();
                
                try (JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(strWriter))
                {
                    jsonGenerator.useDefaultPrettyPrinter();

                    jsonGenerator.writeStartObject();
                    
                    JsonHelper.hashMapToJson(jsonGenerator, serverHashMap);
                 
                    jsonGenerator.writeFieldName("getChannels");
                    jsonGenerator.writeObject(context.getBaseUri() + "virtualservers/" + strVirtualServerId + "/channels");
                    jsonGenerator.writeFieldName("getClients");
                    jsonGenerator.writeObject(context.getBaseUri() + "virtualservers/" + strVirtualServerId + "/clients");
                                        
                    jsonGenerator.writeEndObject();
                    
                    jsonGenerator.close();
                    
                    return Response.ok(strWriter.toString(), MediaType.APPLICATION_JSON).build();
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
            Logger.getLogger(VirtualServersResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.NOT_FOUND).entity("404 NOT FOUND").build();
        }
        catch (Exception ex)
        {
            Logger.getLogger(VirtualServersResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Could not connect to Team Speak server").build();
        }
    }
}

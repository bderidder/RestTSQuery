package org.coderspotting.ts.query.rest.resources;

import java.io.IOException;
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
import org.coderspotting.ts.query.rest.server.ServerQuery;
import org.coderspotting.ts.query.rest.server.VirtualServerDoesNotExistException;
import org.coderspotting.ts.query.rest.server.command.ChannelListCommand;

@Path("/virtualservers/{id}/channels")
public class ChannelsResource
{
    @Context
    private UriInfo context;

    public ChannelsResource()
    {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChannels(@PathParam("id") int virtualServer)
    {
        try
        {
            ServerQuery query = new ServerQuery();

            ChannelListCommand cmd = new ChannelListCommand();

            query.doListCommand(cmd, virtualServer);

            List<HashMap<String, String>> channels = cmd.getRawOutput();
            
            try
            {
              String json = JsonHelper.hashMapListToJson(channels);
              
              return Response.ok(json, MediaType.APPLICATION_JSON).build();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ChannelsResource.class.getName()).log(Level.SEVERE, null, ex);

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            } 
        }
        catch (VirtualServerDoesNotExistException ex)
        {
            Logger.getLogger(ChannelsResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.NOT_FOUND).entity("404 NOT FOUND - Virtual Server " + virtualServer).build();
        }
        catch (Exception ex)
        {
            Logger.getLogger(ChannelsResource.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}

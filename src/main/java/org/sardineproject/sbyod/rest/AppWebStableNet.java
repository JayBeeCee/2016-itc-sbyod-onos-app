package org.sardineproject.sbyod.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onlab.packet.Ip4Address;
import org.onlab.packet.TpPort;
import org.onosproject.rest.AbstractWebResource;
import org.sardineproject.sbyod.configJob.configJob;
import org.slf4j.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Define the StableNet Server IP and Port
 */
@Path("/stablenet")
public class AppWebStableNet extends AbstractWebResource {

    private static final Logger log = getLogger(configJob.class);

    private static final String INVALID_PARAMETER = "INVALID_PARAMETER\n";
    private final ObjectNode ENABLED_TRUE = mapper().createObjectNode().put("enabled", true);
    private final ObjectNode ENABLED_FALSE = mapper().createObjectNode().put("enabled", false);

    /**
     * Define the destination where the StableNet server is running.
     * @param ip ip address of the server
     * @param port transport protocol port of the server
     * @return enabled true if portal was defined right
     */
    @POST
    @Path("/ip/{ip}/port/{port}")
    public Response setPortal(@PathParam("ip") String ip,
                              @PathParam("port") String port) {
        log.info("AppWebStableNet: Adding StableNet server with IP = {} and port = {}", ip, port);

        if (ip == null || port == null)
            return Response.status(Response.Status.PRECONDITION_FAILED).build();

        Ip4Address ip4Address;
        TpPort tpPort;
        try{
            ip4Address = Ip4Address.valueOf(ip);
            tpPort = TpPort.tpPort(Integer.valueOf(port));
        } catch (Exception e){
            return Response.status(Response.Status.PRECONDITION_FAILED).build();
        }

        if(get(configJob.class).setStableNetIp_Port(ip4Address.toString(), tpPort.toString()))
            return Response.ok(ENABLED_TRUE).build();
        else
            return Response.ok(ENABLED_FALSE).build();

    }
}

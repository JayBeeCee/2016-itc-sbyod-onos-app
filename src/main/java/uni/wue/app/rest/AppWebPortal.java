/*
 * Copyright 2015 Lorenz Reinhart
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
package uni.wue.app.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.onlab.packet.Ip4Address;
import org.onlab.packet.TpPort;
import org.onosproject.rest.AbstractWebResource;
import org.slf4j.Logger;
import uni.wue.app.PortalService;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Web portal.
 */
@Path("/portal")
public class AppWebPortal extends AbstractWebResource {

    private static final Logger log = getLogger(uni.wue.app.PortalManager.class);

    private static final String INVALID_PARAMETER = "INVALID_PARAMETER\n";
    private final ObjectNode ENABLED_TRUE = mapper().createObjectNode().put("enabled", true);
    private final ObjectNode ENABLED_FALSE = mapper().createObjectNode().put("enabled", false);

    @POST
    @Path("/ip/{ip}/port/{port}")
    public Response setPortal(@PathParam("ip") String ip,
                              @PathParam("port") String port) {
        log.debug("AppWebPortal: Adding portal with IP = {} and port = {}", ip, port);

        if (ip == null || port == null)
            return Response.ok(INVALID_PARAMETER).build();

        Ip4Address ip4Address;
        TpPort tpPort;
        try{
            ip4Address = Ip4Address.valueOf(ip);
            tpPort = TpPort.tpPort(Integer.valueOf(port));
        } catch (Exception e){
            return Response.ok(INVALID_PARAMETER).build();
        }

        if(get(PortalService.class).setPortal(ip4Address, tpPort))
            return Response.ok(ENABLED_TRUE).build();
        else
            return Response.ok(ENABLED_FALSE).build();

    }
}

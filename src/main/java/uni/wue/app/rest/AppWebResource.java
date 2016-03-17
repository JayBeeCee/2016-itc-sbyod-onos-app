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
import com.google.common.collect.Sets;
import org.onlab.packet.Ip4Address;
import org.onosproject.net.Host;
import org.onosproject.net.host.HostService;
import org.onosproject.rest.AbstractWebResource;

import org.slf4j.Logger;
import uni.wue.app.connection.Connection;
import uni.wue.app.connection.ConnectionStore;
import uni.wue.app.connection.DefaultConnection;
import uni.wue.app.service.Service;
import uni.wue.app.service.ServiceId;
import uni.wue.app.service.ServiceStore;

import static org.slf4j.LoggerFactory.getLogger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Web resource.
 */
@Path("")
public class AppWebResource extends AbstractWebResource {

    private static final Logger log = getLogger(uni.wue.app.PortalManager.class);

    private static final String INVALID_PARAMETER = "INVALID_PARAMETER\n";
    private final ObjectNode ENABLED_TRUE = mapper().createObjectNode().put("enabled", true);
    private final ObjectNode ENABLED_FALSE = mapper().createObjectNode().put("enabled", false);

    /**
     * Get all registered services.
     *
     * @return array of services
     */
    @GET
    @Path("/service")
    public Response getServices(){
        log.debug("AppWebResource: Getting all services");

        Iterable<Service> services = get(ServiceStore.class).getServices();
        return Response.ok(encodeArray(Service.class, "services", services)).build();
    }

    /**
     * Get the service with serviceId.
     *
     * @param serviceId_ the ID of the service
     * @return INVALID_PARAMETER if some parameter was wrong
     *          service
     */
    @GET
    @Path("/service/{serviceId}")
    public Response getService(@PathParam("serviceId") String serviceId_){
        log.debug("AppWebResource: Getting service with name = {}", serviceId_);

        if(serviceId_ == null)
            return Response.ok(INVALID_PARAMETER).build();

        Service service = get(ServiceStore.class).getService(ServiceId.serviceId(serviceId_));
        Set<Service> result = (service == null) ? Sets.newHashSet() : Sets.newHashSet(service);

        return Response.ok(encodeArray(Service.class, "services", (Iterable)result)).build();
    }

    /**
     * Get the services the user with userIp is connected to.
     *
     * @param userIp_ the IP address of the user
     * @return INVALID_PARAMETER if some parameter was wrong
     *          array of services
     */
    @GET
    @Path("/user/{userIp}")
    public Response getUserRules(@PathParam("userIp") String userIp_){
        log.debug("AppWebResource: Getting services for userIp = {}", userIp_);

        if(userIp_ == null)
            return Response.ok(INVALID_PARAMETER).build();

        Ip4Address userIp;
        try{
            userIp = Ip4Address.valueOf(userIp_);
        } catch (Exception e){
            return Response.ok(INVALID_PARAMETER).build();
        }

        Iterable<Service> services = get(ConnectionStore.class).getUserConnections(userIp).stream()
                .map(c -> c.getService())
                .collect(Collectors.toSet());
        return Response.ok(encodeArray(Service.class, "services", services)).build();
    }

    /**
     * Ask if a user with userIp has a access to the service with serviceId.
     *
     * @param userIp_ the IP address of the user
     * @param serviceId_ the ID of the service
     * @return INVALID_PARAMETER if some parameter was wrong
     *          "enabled : false" if service is disabled
     *          "enabled : true" if service is enabled
     */
    @GET
    @Path("/user/{userIp}/service/{serviceId}")
    public Response getUserServices(@PathParam("userIp") String userIp_,
                                    @PathParam("serviceId") String serviceId_){
        log.debug("AppWebResource: Getting rules for userIp = {} and serviceId = {}", userIp_, serviceId_);

        if(userIp_ == null || serviceId_ == null)
            return Response.ok(INVALID_PARAMETER).build();

        Ip4Address userIp;
        ServiceId serviceId;
        try{
            userIp = Ip4Address.valueOf(userIp_);
            serviceId = ServiceId.serviceId(serviceId_);
        } catch (Exception e){
            return Response.ok(INVALID_PARAMETER).build();
        }

        Set<Connection> result = get(ConnectionStore.class).getUserConnections(userIp);
        if(result.stream()
                .filter(c -> c.getService().id().equals(serviceId))
                .count() != 0){
            return Response.ok(ENABLED_TRUE).build();
        }
        return Response.ok(ENABLED_FALSE).build();
    }

    /**
     * Allow a host with userIP to send packets to service with serviceId.
     *
     * @param userIp_ the IP address of the user
     * @param serviceId_ the ID of the service
     * @return INVALID_PARAMETER if some parameter was wrong
     *          "enabled : false" if service connection went wrong
     *          "enabled : true" if service is enabled
     */
    @POST
    @Path("/user/{userIp}/service/{serviceId}")
    public Response allowHostTraffic(@PathParam("userIp") String userIp_,
                                    @PathParam("serviceId") String serviceId_){
        log.debug("AppWebResource: Adding connection between user ip = {} and serviceId = {}",
                new String[]{userIp_, serviceId_});

        if(userIp_ == null || serviceId_ == null)
            return Response.ok(INVALID_PARAMETER).build();

        Service service;
        Set<Host> srcHosts;
        try{
            Ip4Address userIp = Ip4Address.valueOf(userIp_);
            srcHosts = get(HostService.class).getHostsByIp(userIp);
            service = get(ServiceStore.class).getService(ServiceId.serviceId(serviceId_));
        } catch (Exception e){
            return Response.ok(INVALID_PARAMETER).build();
        }

        if(service == null) {
            log.debug("AppWebResource: No service found with id = {}", serviceId_);
            return Response.ok(ENABLED_FALSE).build();
        }

        // install connection for every host and service
        for(Host srcHost : srcHosts) {
                Connection connection = new DefaultConnection(srcHost, service);
                // if the connection does not already exist
                if (!get(ConnectionStore.class).contains(connection)) {
                    log.debug("AppWebResource: Installing connection {}", connection.toString());
                    get(ConnectionStore.class).addConnection(connection);
                } else{
                    log.debug("AppWebResource: Connection {} already exists", connection.toString());
                }
        }

        return Response.ok(ENABLED_TRUE).build();
    }
}

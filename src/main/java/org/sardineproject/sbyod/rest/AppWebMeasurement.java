package org.sardineproject.sbyod.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onosproject.rest.AbstractWebResource;
import org.sardineproject.sbyod.measurement.Measurement;
import org.sardineproject.sbyod.portal.PortalManager;
import org.slf4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Define Settings for Measurements
 */
@Path("/measurements")
public class AppWebMeasurement extends AbstractWebResource {

    private static final Logger log = getLogger(PortalManager.class);

    private static final String INVALID_PARAMETER = "INVALID_PARAMETER\n";
    private final ObjectNode ENABLED_TRUE = mapper().createObjectNode().put("enabled", true);
    private final ObjectNode ENABLED_FALSE = mapper().createObjectNode().put("enabled", false);


    /**
     * Define the logFile Location.
     * @param location location of the logFile
     * @return enabled true if logFile got saved
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{location}")
    public Response setLogFile(@PathParam("location") String location) {

        log.info("AppWebMeasurements: Location{}", location);

        Measurement measurement = get(Measurement.class);
        measurement.setLogFile(location);
        return Response.ok(ENABLED_TRUE).build();
    }

    /**
     * Define the logFile Location
     * @return logFile location
     */
    @GET
    @Path("/")
    public Response getLogFile() {

        Measurement measurement = get(Measurement.class);
        String location = measurement.getLogFile();
        return Response.ok(location).build();
    }

}



package org.sardineproject.sbyod.rest;

import com.fasterxml.jackson.databind.deser.std.MapEntryDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onosproject.rest.AbstractWebResource;
import org.sardineproject.sbyod.measurement.Measurement;
import org.sardineproject.sbyod.portal.PortalManager;
import org.slf4j.Logger;

import javax.print.DocFlavor;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Define Settings for Measurements
 */
@Path("/measurements")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
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
    @Path("/{location}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response setLogFile(@PathParam("location") String location) {

        log.info("AppWebMeasurements_POST: Location{}", location);

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
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getLogFile() {

        Measurement measurement = get(Measurement.class);
        String location = measurement.getLogFile();
        log.info("AppWebMeasurements_GET: Location{}", location);
        return Response.ok(location.toString()).build();
    }

}



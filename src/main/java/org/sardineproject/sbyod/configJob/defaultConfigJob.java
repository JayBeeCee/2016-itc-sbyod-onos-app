package org.sardineproject.sbyod.configJob;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.apache.felix.scr.annotations.*;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.sardineproject.sbyod.StableNet.StableNetConnection;
import org.sardineproject.sbyod.measurement.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.onosproject.cli.AbstractShellCommand.get;

/**
 * Created by Bene on 11.01.17.
 */
@Component(immediate = true)
@org.apache.felix.scr.annotations.Service
public class defaultConfigJob implements configJob{

    private final Logger log = LoggerFactory.getLogger(getClass());
    public ApplicationId appId;
    private String snIP = "127.0.0.1";
    private String snPort = "5443";
    private StableNetConnection sn;

    private String username = "infosim";
    private String password = "stablenet";

    private boolean measurementFlag = false;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected CoreService coreService;

    @Activate
    protected void activate() {
        appId = coreService.registerApplication(configJob.APP_ID);
        log.info("Started ConfigJobService {}", appId.toString());
    }

    @Deactivate
    protected void deactivate() {
        log.info("Stopped ConfigJobService {}", appId.toString());
    }

    @Override
    public void startConfigJob_conEstablish() {

        this.sn = new StableNetConnection("https://" + this.snIP + ":" + this.snPort, this.username, this.password);
        log.info("StableNet server: {}", this.sn);
        String restURL = "/rest/jobs/start/";
        String configJobID = "1039";
        log.info("GET -> {}", this.sn.getServer() + restURL + configJobID);
        WebResource webResource = this.sn.getClient().resource(this.sn.getServer() + restURL + configJobID);


        // time logging
        Measurement measurement = get(Measurement.class);
        measurement.logEndTime();

        // do REST call
        ClientResponse response = webResource.accept("application/xml").get(ClientResponse.class);

        if (response.getStatus() != 200)
        {
            throw new RuntimeException("Connection Failed - HTTP error code: " + response.getStatus());
        }

    }

    @Override
    public void startConfigJob_conRemove(){


        // do prepare REST call
        this.sn = new StableNetConnection("https://" + this.snIP + ":" + this.snPort, this.username, this.password);
        String restURL = "/rest/jobs/start/";
        String configJobID = "1041";
        WebResource webResource = this.sn.getClient().resource(this.sn.getServer() + restURL + configJobID);

//        String server = "http://" + this.snIP.toString() + ":" + this.snPort.toString();
//        HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter(this.username, this.password);
//        String restURL = "/rest/jobs/start/";
//        String configJobID = "1041";
//        Client client = Client.create();
//        client.addFilter(authFilter);
//        WebResource webResource = client.resource(server + restURL + configJobID);

        // time logging
        Measurement measurement = get(Measurement.class);
        measurement.logEndTime();

        // do REST call
        ClientResponse response = webResource.accept("application/xml").get(ClientResponse.class);

        if (response.getStatus() != 200)
        {
            throw new RuntimeException("Connection Failed - HTTP error code: " + response.getStatus());
        }

        //JsonParser jsonParser = new JsonParser();
        //JsonObject garbage = jsonParser.parse(response.getEntity(String.class)).getAsJsonObject();
    }

    @Override
    public boolean setStableNetIp_Port(String stableNetIp, String stableNetPort){
        this.snIP = stableNetIp;
        this.snPort = stableNetPort;
        return true;
    }

}

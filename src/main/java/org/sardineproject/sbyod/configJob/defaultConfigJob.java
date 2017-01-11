package org.sardineproject.sbyod.configJob;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.apache.felix.scr.annotations.*;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
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
    private String snIP = "172.16.150.";
    private String snPort = "5443";

    private String username = "Infosim";
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
        String newLine = "\n";
        Measurement measurementObj = get(Measurement.class);
        String logFile = measurementObj.getLogFile();
        File file = new File(logFile);
        PrintWriter printWriter = null;

        // prepare REST call
        String server = "https://" + this.snIP + ":" + this.snPort;
        HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter(username, password);
        String restURL = "/rest/jobs/start/";
        String configJobID = "1039";
        Client client = Client.create();
        client.addFilter(authFilter);
        WebResource webResource = client.resource(server + restURL + configJobID);

        // log time of REST call
        try{
            if(file.exists()) {
                String currentTime = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
                printWriter = new PrintWriter((new FileOutputStream(logFile, true)));
                printWriter.write(currentTime);
                printWriter.write(newLine);
            } else {
                log.debug("defaultConfigJob: File does not exist");
            }
        } catch(IOException ioex) {
            log.debug("defaultConfigJob: Error while writing time into csv file: {}", ioex);
        } finally {
            if(printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }

        // do REST call
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

        if (response.getStatus() != 200)
        {
            throw new RuntimeException("Connection Failed - HTTP error code: " + response.getStatus());
        }
    }

    @Override
    public void startConfigJob_conRemove(){
        String newLine = "\n";
        Measurement measurementObj = get(Measurement.class);
        String logFile = measurementObj.getLogFile();
        File file = new File(logFile);
        PrintWriter printWriter = null;

        // do prepare REST call
        String server = "http://" + this.snIP + ":" + this.snPort;
        HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter(username, password);
        String restURL = "/rest/jobs/start/";
        String configJobID = "1041";
        Client client = Client.create();
        client.addFilter(authFilter);
        WebResource webResource = client.resource(server + restURL + configJobID);

        //log time of REST call
        try{
            if(file.exists()) {
                String currentTime = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
                printWriter = new PrintWriter((new FileOutputStream(logFile, true)));
                printWriter.write(currentTime);
                printWriter.write(newLine);
            } else {
                log.debug("defaultConfigJob: File does not exist");
            }
        } catch(IOException ioex) {
            log.debug("defaultConfigJob: Error while writing time into csv file: {}", ioex);
        } finally {
            if(printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }

        // do REST call
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);


        if (response.getStatus() != 200)
        {
            throw new RuntimeException("Connection Failed - HTTP error code: " + response.getStatus());
        }

    }

    @Override
    public boolean setStableNetIp_Port(String stableNetIp, String stableNetPort){
        this.snIP = stableNetIp;
        this.snPort = stableNetPort;
        return true;
    }

}

package org.sardineproject.sbyod.measurement;

import org.apache.felix.scr.annotations.*;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Bene on 29.12.16.
 */

@Component(immediate = true)
@org.apache.felix.scr.annotations.Service
public class MeasurementExtension implements Measurement{

    private final Logger log = LoggerFactory.getLogger(getClass());
    public ApplicationId appId;
    private String logFile = "/home/vagrant/measurements/measurement.csv";
    private String csvSeparator = ",";
    private String newLine = "\n";


    private boolean measurementFlag = false;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected CoreService coreService;

    @Activate
    protected void activate() {
        appId = coreService.registerApplication(Measurement.APP_ID);
        log.info("Started MeasurementExtension {}", appId.toString());
    }

    @Deactivate
    protected void deactivate() {
        log.info("Stopped MeasurementExtension {}", appId.toString());
    }

    @Override
    public void logStartTime(boolean status_connected){
        String status = "connected";

        if( status_connected == false){
            status = "disconnected";
        }

        PrintWriter printWriter = null;
        File file = new File(this.logFile);

        try{
            if(file.exists()) {
                // write request timestamp to logFile
                String currentTime = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
                printWriter = new PrintWriter((new FileOutputStream(logFile, true)));
                printWriter.write(status);
                printWriter.write(this.csvSeparator);
                printWriter.write(currentTime);
                printWriter.write(this.csvSeparator);
            } else {
                log.debug("MeasurementExtension: File does not exist");
            }
        } catch(IOException ioex) {
            log.debug("MeasurementExtension: Error while writing time into csv file: {}", ioex);
        } finally {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
                //TODO make this flag REST accessable
                setFlag(true);
            }
        }
    }

    @Override
    public void logEndTime(){
        PrintWriter printWriter = null;
        File file = new File(this.logFile);

        try{
            if(file.exists()) {
                // write request timestamp to logFile
                String currentTime = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
                printWriter = new PrintWriter((new FileOutputStream(logFile, true)));
                printWriter.write(currentTime);
                printWriter.write(this.newLine);
            } else {
                log.debug("MeasurementExtension: File does not exist");
            }
        } catch(IOException ioex) {
            log.debug("MeasurementExtension: Error while writing time into csv file: {}", ioex);
        } finally {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
                //TODO make this flag REST accessable
                //setFlag(false);
            }
        }

    }

    @Override
    public void setFlag(boolean newStatus){
        this.measurementFlag = newStatus;
    }

    @Override
    public boolean getFlag(){
        return this.measurementFlag;
    }

    @Override
    public void setLogFile(String logFile){
        this.logFile = logFile;
    }

    @Override
    public String getLogFile(){
        return this.logFile;
    }
}

package org.sardineproject.sbyod.measurement;

import org.apache.felix.scr.annotations.*;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Bene on 29.12.16.
 */

@Component(immediate = true)
@org.apache.felix.scr.annotations.Service
public class MeasurementExtension implements Measurement{

    private final Logger log = LoggerFactory.getLogger(getClass());
    public ApplicationId appId;
    public String logFile = "/home/vagrant/measurements/measurement.csv";

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

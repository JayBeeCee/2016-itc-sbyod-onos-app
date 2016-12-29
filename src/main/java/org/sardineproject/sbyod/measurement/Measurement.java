package org.sardineproject.sbyod.measurement;

/**
 * Created by Bene on 29.12.16.
 */
public interface Measurement {
    String APP_ID = "org.sardineproject.sbyod";

    void setFlag(boolean newFlag);

    boolean getFlag();



}

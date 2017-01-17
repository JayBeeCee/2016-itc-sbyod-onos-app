package org.sardineproject.sbyod.configJob;

/**
 * Created by Bene on 11.01.17.
 */
public interface configJob{
    String APP_ID = "org.sardineproject.sbyod";

    void startConfigJob_conEstablish();

    void startConfigJob_conRemove();

    void startDiscoveryJob();

    boolean setStableNetIp_Port(String stableNetIP, String stableNetPort);
}

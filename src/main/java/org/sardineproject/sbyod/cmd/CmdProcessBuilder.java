package org.sardineproject.sbyod.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bene on 16.01.17.
 */
public class CmdProcessBuilder {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private List<String> commandList = new ArrayList<String>();

    public CmdProcessBuilder (String command) {
        this.commandList.add(command);
    }

    public boolean cmdExecute () {
        ProcessBuilder pb = new ProcessBuilder(this.commandList);
        int exitCode = -1;
        try {
            Process p = pb.start();
            exitCode = p.exitValue();
        } catch (IOException e) {
            log.debug("CmdProcessBuilder: script exection failed: {}", e);
        } finally {
            if(exitCode == 0){
                return true;
            } else {
                return false;
            }
        }
    }

}

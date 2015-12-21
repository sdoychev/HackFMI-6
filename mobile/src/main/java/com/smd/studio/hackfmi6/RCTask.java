package com.smd.studio.hackfmi6;

import android.os.AsyncTask;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.util.Properties;

/**
 * Created by Doychev on 13.5.2015.
 */
public class RCTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(Constants.USERNAME, ConnectionConfig.getInstance().getHostAddress(), 22);
            session.setPassword(Constants.PASSWORD);
            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();

            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(params[0]);
            channelExec.connect();
            //channelExec.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return null;
    }
}
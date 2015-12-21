package com.smd.studio.hackfmi6;

import android.app.Application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Doychev on 22.05.2015.
 */

public class ConnectionConfig extends Application {

    private static ConnectionConfig mInstance;
    private static ArrayList<String> ipList = new ArrayList<>();
    private String gatewayAddress;
    private String hostAddress;

    public static synchronized ConnectionConfig getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        gatewayAddress = "192.168.1.1"; //the default gateway address.
        hostAddress = "192.168.1.103";  //the default host address.
        ipList.add(hostAddress);
        obtainHostIpAddress();
    }

    public String getGatewayAddress() {
        return gatewayAddress;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String address) {
        hostAddress = address;
    }

    public ArrayList<String> getIpList() {
        return ipList;
    }

    public void obtainHostIpAddress() {
        BufferedReader br = null;
        boolean isFirstLine = true;
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] splitted = line.split(" +");
                if (splitted.length >= 4 && !splitted[0].equals(ConnectionConfig.getInstance().getGatewayAddress())) {
                    setHostAddress(splitted[0]);
                    ipList.add(splitted[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
package org.opennms.velocloud.client.api.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utils {
    public static InetAddress getValidInet4Address(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            if (address.getHostAddress().equals(ip)) {
                return address;
            } else return null;
        } catch (UnknownHostException ex) {
            return null;
        }
    }
}

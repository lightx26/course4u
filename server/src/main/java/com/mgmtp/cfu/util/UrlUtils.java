package com.mgmtp.cfu.util;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtils {
    public static String standardizeUrl(String urlString) throws MalformedURLException {
        URL urlObj = new URL(urlString);
        return urlObj.getProtocol() + "://" + urlObj.getHost() + urlObj.getPath();
    }

}

package com.incomm.wmp.gprServices.outbound.ingo.transport.http;


import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 *@author allen
 */
public class IngoHttpsUrlConnectionMessageSender extends HttpUrlConnectionMessageSender {

    private Integer connectionTimeout;
    private Integer readTimeout;
    private Boolean shouldVerifyHost;

    @Override
    protected void prepareConnection(HttpURLConnection connection) throws IOException {
        super.prepareConnection(connection);

        if (shouldVerifyHost != null && !shouldVerifyHost) {
            if (connection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connection;
                httpsURLConnection.setReadTimeout(readTimeout);
                httpsURLConnection.setConnectTimeout(connectionTimeout);
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
            }
        }
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Boolean getShouldVerifyHost() {
        return shouldVerifyHost;
    }

    public void setShouldVerifyHost(Boolean shouldVerifyHost) {
        this.shouldVerifyHost = shouldVerifyHost;
    }
}
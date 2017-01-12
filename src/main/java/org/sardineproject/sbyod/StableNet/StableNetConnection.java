package org.sardineproject.sbyod.StableNet;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

import javax.net.ssl.*;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by Bene on 12.01.17.
 */
public class StableNetConnection {

    public static ClientConfig configureClient() {
        TrustManager[] certs = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }
                }
        };
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, certs, new SecureRandom());
        } catch (GeneralSecurityException ex) {
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

        ClientConfig config = new DefaultClientConfig();
        try {
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }, ctx));
        } catch (Exception e) {
        }
        return config;
    }

    private String server;
    private Client client;

    public StableNetConnection(String server, String username, String password) {
        this.server = server;

        HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter(username, password);
        this.client = Client.create(StableNetConnection.configureClient());
        this.client.addFilter(authFilter);
    }

    public String getServer() {
        return this.server;
    }

    public Client getClient() {
        return this.client;
    }

}

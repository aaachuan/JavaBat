package com.aaachuan;

public class HttpServerConfig {
    private String host;
    private int port;
    HttpServerConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }


}

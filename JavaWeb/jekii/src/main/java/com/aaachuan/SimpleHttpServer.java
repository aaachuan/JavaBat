package com.aaachuan;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class SimpleHttpServer {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpServer.class);

    private HttpServerConfig httpServerConfig;
    private HttpServer httpServer;

    public SimpleHttpServer(HttpServerConfig httpServerConfig, HttpServer httpServer) {
        this.httpServerConfig = httpServerConfig;
        this.httpServer = httpServer;
    }

    public void startHttpServer() {
        httpServer.start();
        logger.info("start jekki http server at {}:{}", httpServerConfig.getHost(), httpServerConfig.getPort());
    }

    public void handleService() {
        HttpContext context = httpServer.createContext("/");
        context.setHandler(httpExchange -> {
            //获取请求方法、URI、Path、Query
            String method = httpExchange.getRequestMethod();
            URI uri = httpExchange.getRequestURI();
            String path = uri.getPath();
            String query = uri.getRawQuery();
            logger.info("{}: {}?{}", method, path, query);

            //响应Header
            Headers responseHeaders = httpExchange.getResponseHeaders();
            responseHeaders.set("Content-type", "text/html; charset=utf-8");
            responseHeaders.set("Cache-Control", "no-cache");

            httpExchange.sendResponseHeaders(200, 0);

            String s = "<h1>hello, world.</h1><p>" + LocalDateTime.now().withNano(0) + "</p>";
            try(OutputStream out = httpExchange.getResponseBody()) {
                out.write(s.getBytes(StandardCharsets.UTF_8));
            }

        });
    }

    public void stopHttpServer() {
        httpServer.stop(3);
    }


    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 7777;
        HttpServerConfig config = new HttpServerConfig(host, port);
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);
        SimpleHttpServer simpleHttpServer = new SimpleHttpServer(config,server);
        simpleHttpServer.startHttpServer();
        simpleHttpServer.handleService();
    }
}

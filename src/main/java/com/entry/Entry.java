package com.entry;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.Executors;

import static com.entry.MiddleWare.MIDDLE_WARE;

public class Entry {

    private static HttpServer httpServer;
    private static String message;
    /*
    Init block here will be run at build time, the result will be placed
    in am image space. This space is loaded at runtime.
     */
    static{
        System.out.println("I am building");
        message = String.format("This is a reply from %s",System.getProperty("user.name"));
        System.out.println("I have built");
    }

    public static void main(String[] args) throws IOException {
        var provider = HttpServerProvider.provider();
        var listeningPort = getPort();
        httpServer = provider.createHttpServer(new InetSocketAddress(listeningPort),0);
        httpServer.setExecutor(Executors.newFixedThreadPool(10));

        // This and its middleware will catch everything unless the route is caught by another context
        var rootContext = httpServer.createContext("/",(exchange)->{
            exchange.sendResponseHeaders(200,0);
            exchange.close();
        });
        rootContext.getFilters().addAll(MIDDLE_WARE);

        var whoAmIContext = httpServer.createContext("/whoami",(exchange)->{
            var messageBytes = message.getBytes();
            var responseWriter = exchange.getResponseBody();
            exchange.sendResponseHeaders(200,messageBytes.length);
            responseWriter.write(messageBytes);
            responseWriter.flush();
            exchange.close();
        });
        whoAmIContext.getFilters().addAll(MIDDLE_WARE);

        httpServer.start();
    }

    private static Integer getPort() {
        try{
            var port = System.getenv().get("PORT");
            if(port == null){
             throw new IllegalArgumentException("env var PORT needs to be defined");
            }
            return Integer.parseInt(System.getenv().get("PORT"));
        }catch (NumberFormatException e){
            throw new NumberFormatException("env var PORT needs to be a number");
        }
    }
}

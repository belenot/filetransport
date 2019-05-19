package com.belenot.filetransport;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import com.belenot.filetransport.util.logging.ServerLogger;
import com.belenot.filetransport.util.logging.ServerLoggerHandler;

@Configurable
public class ServerConfig {
    public static void main(String[] args) {
	ApplicationContext ctx = new AnnotationConfigApplicationContext(ServerConfig.class);	
    }

    @Bean( initMethod = "start" )
    public Server server() {
	Server server = new Server();
	server.setLogger(logger());
	server.setServerSocket(serverSocket());
	server.setSoTimeout(1000);
	server.setExecutorService(executorService());
	
	return server;
    }

    @Bean
    public ServerLogger logger() {
	String name = "ServerLogger";
	String resourceBundleName = null;
	return new ServerLogger(name, resourceBundleName, serverLoggerHandler());
    }

    @Bean
    public ServerLoggerHandler serverLoggerHandler() {
	return new ServerLoggerHandler(System.err);
    }

    @Bean
    public ExecutorService executorService() {
	ExecutorService executorService = Executors.newCachedThreadPool();
	return executorService;
    }

    @Bean
    public ServerSocket serverSocket() {
	try {
	    ServerSocket serverSocket = new ServerSocket(5678);
	    return serverSocket;
	} catch (IOException exc) {
	    return null;
	}
    }

    @Bean( initMethod="init" )
    public CommandReader commandReader() {
	CommandReader commandReader = new CommandReader();
	commandReader.setInputStream(System.in);
	Set<CommandEventListener> listeners = new HashSet<>();
	listeners.add(server());
	commandReader.setListeners(listeners);
	return commandReader;
    }

    
    
}

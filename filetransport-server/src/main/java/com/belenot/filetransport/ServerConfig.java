package com.belenot.filetransport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.belenot.filetransport.aspect.ServerAspects;
import com.belenot.filetransport.util.logging.ServerLogger;
import com.belenot.filetransport.util.logging.ServerLoggerHandler;

@Configurable
@PropertySource( "classpath:/server.properties" )
@EnableAspectJAutoProxy
public class ServerConfig {
    private static int DEFAULT_SO_TIMEOUT = 1000;
    private static int DEFAULT_SERVERSOCKET_PORT = 5678;
    @Autowired
    private Environment env;
    @Autowired
    private ApplicationContext ctx;
    
    public static void main(String[] args) {
	ApplicationContext ctx = null;
	ctx = new AnnotationConfigApplicationContext(ServerConfig.class);
	Startable server = (Startable) ctx.getBean("server");
	server.start();
	
    }
    
    @Bean
    public ServerAspects serverAspects() {
	return new ServerAspects();
    }
    
    @Bean 
    public Server server() {
	Server server = new Server();
	int soTimeout = env.getProperty("server.soTimeout", Integer.class, DEFAULT_SO_TIMEOUT);
	server.setLogger(logger());
	server.setServerSocket(serverSocket());
	server.setSoTimeout(soTimeout);
	server.setClientServiceFactory(clientServiceFactory());
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
	return new ServerLoggerHandler(stderr());
    }

    @Bean
    public ExecutorService executorService() {
	ExecutorService executorService = Executors.newCachedThreadPool();
	return executorService;
    }

    @Bean
    public ServerSocket serverSocket() {
	int serverSocketPort = env.getProperty("server.serverSocketPort", Integer.class, DEFAULT_SERVERSOCKET_PORT);
	try {
	    ServerSocket serverSocket = new ServerSocket(serverSocketPort);
	    return serverSocket;
	} catch (IOException exc) {
	    return null;
	}
    }

    @Bean( initMethod="init" )
    public CommandReader commandReader() {
	CommandReader commandReader = new CommandReader();
	commandReader.setInputStream(stdin());
	return commandReader;
    }

    @Bean
    public ClientServiceFactory clientServiceFactory() {
	ClientServiceFactory clientServiceFactory = new ClientServiceFactory();
	clientServiceFactory.setServerLogger(logger());
	return clientServiceFactory;
    }

    /**
       in server.properties would be URLs to stdin stdout and stderr streams.
       need to read that url. if no given, state for default(System.in/out/err)
       if url not correct, state for default
     */
    @Bean
    public InputStream stdin() { return System.in; }
    @Bean
    public OutputStream stdout() { return System.out; }
    @Bean
    public OutputStream stderr() { return System.err; }
    
}

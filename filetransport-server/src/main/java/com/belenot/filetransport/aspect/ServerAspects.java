package com.belenot.filetransport.aspect;

import java.util.Date;
import java.util.logging.Level;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.belenot.filetransport.annotation.Nameable;
import com.belenot.filetransport.util.logging.ServerLogger;

@Aspect
public class ServerAspects {
    @Autowired
    private ServerLogger logger;
    @Before( "execution(* start(..)) || execution(* run(..))" )
    public Object startAdvice(JoinPoint jp) throws Throwable {
	String methodName = jp.getSignature().getName();
	String typeName = jp.getTarget().getClass().getName();
	String instanceName = "noname";
	if (jp.getTarget() instanceof Nameable) {
	    instanceName = ((Nameable) jp.getTarget()).getName();
	}
        String date = (new Date()).toString();
	String logMessage = String.format("{%s} %s.%s[%s]", date, typeName, methodName, instanceName);
	logger.log(Level.INFO, logMessage);
	return null;
    }
}

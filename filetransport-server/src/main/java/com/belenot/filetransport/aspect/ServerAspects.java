package com.belenot.filetransport.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Aspect
public class ServerAspects {
    @Around( "execution(* start())" )
    public Object startAdvice(ProceedingJoinPoint pjp) throws Throwable {
	System.out.println("ADVICE START");
	pjp.proceed();
	System.out.println("ADVICE END");
	return null;
    }
}

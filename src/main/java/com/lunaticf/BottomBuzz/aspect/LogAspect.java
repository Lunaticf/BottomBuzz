package com.lunaticf.BottomBuzz.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.util.Date;


@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.lunaticf.BottomBuzz.controller.HomeController.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        logger.info("start exectutefewfewfewf" + new Date());
    }

    @After("execution(* com.lunaticf.BottomBuzz.controller.HomeController.*(..))")
    public void afterMethod(JoinPoint joinPoint) {
        logger.info("start exectute" + new Date());
    }

}

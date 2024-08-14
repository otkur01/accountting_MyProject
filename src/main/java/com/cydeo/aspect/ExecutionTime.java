package com.cydeo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTime {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionTime.class);

    @Pointcut("@annotation(com.cydeo.annotation.ExecutionTime)")
    public void executionTimeAnnotation(){}

    @Around("executionTimeAnnotation()")
    public Object calculateExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.info("Before-> method started :{}", proceedingJoinPoint.getSignature().toShortString());
        long startTime = System.currentTimeMillis();

        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Exception in method execution: ", throwable);
            throw throwable;
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            logger.info("Execution time of method  is : {} ms ",
                    executionTime);
        }

        return result;
    }





}
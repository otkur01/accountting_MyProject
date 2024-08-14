package com.cydeo.aspect;


import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.UserPrincipal;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ActivateDeactivateCompany {

    Logger logger = LoggerFactory.getLogger(ActivateDeactivateCompany.class);

//    private UserPrincipal getUser(){
//        UserPrincipal userPrincipal =(UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return userPrincipal;
//    }
//    @Pointcut("@annotation(com.cydeo.annotation.ActiveDeActive)")
//    public void activeDeactivatedAnnotation() {}
//
//    @Around("activeDeactivatedAnnotation()")
//    public Object logActiveDeactivatedAnnotation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        logger.info("before ->  method :{}",proceedingJoinPoint.getSignature().toShortString());
//        CompanyDto result = null;
//        try {
//            result = (CompanyDto) proceedingJoinPoint.proceed();
//        } catch (Throwable throwable) {
//            logger.error("Exception in method execution: ", throwable);
//            throw throwable;
//        } finally {
//            logger.info("After-> method : {}, Logged User Name: {}, activated Company name is:  {}",
//                    proceedingJoinPoint.getSignature().getDeclaringTypeName(),
//                    getUser().getFullNameForProfile()
//                    ,result.getTitle()
//            );
//        }
//
//        return result;
//
//    }



}

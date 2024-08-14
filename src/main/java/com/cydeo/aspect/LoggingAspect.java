package com.cydeo.aspect;


import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.UserPrincipal;
import com.cydeo.service.SecurityService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
//    private final SecurityService securityService;
    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

private UserPrincipal getUser(){
    UserPrincipal userPrincipal =(UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userPrincipal;
}
    @Pointcut("execution(* com.cydeo.service.impl.CompanyServiceImpl.activeCompanyStatus(..))")
    public void activeCompanyLogAnno(){}
    @AfterReturning(pointcut = "activeCompanyLogAnno()", returning = "result")
    public void afterReturningActivateCompanyAnnotation(JoinPoint joinPoint, CompanyDto result) {
        logger.info("After -> Method: {}, Activated Company : {}, user name: {}",
                joinPoint.getSignature().toShortString()
                , result.getTitle()
                , getUser().getFullNameForProfile()
        );
    }

    @Pointcut("execution(* com.cydeo.service.impl.CompanyServiceImpl.deactivateCompanyStatus(..))")
    public void deactivateCompanyLogAnno(){}
    @AfterReturning(pointcut = "deactivateCompanyLogAnno()", returning = "result")
    public void afterReturningDeactivateCompanyAnnotation(JoinPoint joinPoint, CompanyDto result) {
        logger.info("After -> Method: {}, Deactivated Company : {}, user name: {}",
                joinPoint.getSignature().toShortString()
                , result.getTitle()
                , getUser().getFullNameForProfile());
    }

//    @Pointcut("execution(* com.cydeo..*(..))")
//    public void applicationMethodPointcut() {}
//    @AfterThrowing(pointcut = "applicationMethodPointcut()", throwing = "exception")
//    public void afterThrowingGetMappingOperation(JoinPoint joinPoint, RuntimeException exception) {
//        logger.error("After Throwing -> Name of Exception :{}, Exception: {}"
//                , exception.getClass().getSimpleName(), exception.getMessage());
//
//    }


}

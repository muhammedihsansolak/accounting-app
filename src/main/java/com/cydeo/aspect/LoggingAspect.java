package com.cydeo.aspect;

import com.cydeo.entity.common.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    private UserPrincipal getCompany() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserPrincipal) authentication.getPrincipal());
    }

    private String getUserFirstNameLastNameAndUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserPrincipal) authentication.getPrincipal()).getFullNameForProfile() + " " + authentication.getName();
    }

    @Pointcut("execution(* com.cydeo.controller.CompanyController.activateCompany(..))")
    public void loggingCompanyActivation() {
    }

    @Pointcut("execution(* com.cydeo.controller.CompanyController.deactivateCompany(..))")
    public void loggingCompanyDeactivation() {
    }

    @AfterReturning(pointcut = "loggingCompanyActivation()", returning = "results")
    public void afterLoggingCompanyActivation(JoinPoint joinPoint, Object results) {
        log.info("After Returning -> Method name: {}, Company Name: {}, First Name, Last Name and username of logged-in user: {}",
                joinPoint.getSignature().toShortString()
                , getCompany().getCompanyTitleForProfile()
                , getUserFirstNameLastNameAndUserName()
                , results.toString());
    }

    @AfterReturning(pointcut = "loggingCompanyDeactivation()", returning = "results")
    public void afterLoggingCompanyDeactivation(JoinPoint joinPoint, Object results) {
        log.info("After Returning -> Method name: {}, Company Name: {}, First Name, Last Name and username of logged-in user: {}",
                joinPoint.getSignature().toShortString()
                , getCompany().getCompanyTitleForProfile()
                , getUserFirstNameLastNameAndUserName()
                , results.toString());

    }

    @AfterThrowing(pointcut = "execution(* com.cydeo..*.*(..))", throwing = "runtimeException")
    public void afterThrowingException(JoinPoint joinPoint, RuntimeException runtimeException) {
        log.error("After Throwing -> Method name: {}, Exception: {}, Message: {}",
                joinPoint.getSignature().toShortString(),
                runtimeException.getClass().getSimpleName(),
                runtimeException.getMessage());
    }
}

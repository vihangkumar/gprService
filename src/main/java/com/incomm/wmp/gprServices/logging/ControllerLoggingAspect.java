package com.incomm.wmp.gprServices.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.imp.neo.datamasking.json.JsonPayloadMasker;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Created by dvontela on 7/21/2017.
 */

@Aspect
@Component
public class ControllerLoggingAspect {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    JsonPayloadMasker jsonPayloadMasker;

    ObjectMapper mapper = new ObjectMapper();

    @Pointcut("execution(* com.incomm.gprServices.controller.*.*(..))")
    public void controller() {
    }

    @Before("controller()")
    public void aroundControllerMethod(JoinPoint joinPoint) throws Throwable {
        if(joinPoint.getArgs()[0].toString().contains("-")){
            logger.info("not logging the session id");
        }else{
            logger.info("########################### Request Payload #####################################");
            logger.info(jsonPayloadMasker.getMasked(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(joinPoint.getArgs()[0])));
            logger.info("###########################  #####################################");
        }

    }

    @AfterReturning(value = "controller()", returning = "response")
    public void afterControllerMethod(JoinPoint joinPoint, ResponseEntity response) throws Throwable {
        logger.info("BaseResponse status code");
        logger.info(response.getStatusCode().toString());
        if (response.getBody() != null) {
            if (response.getBody().toString().contains("base64")) {
                logger.info("not logging the base 64 image payload");
            } else if (response.getStatusCode().equals(HttpStatus.OK)) {
                logger.info("########################### Response Payload #####################################");
                logger.info(jsonPayloadMasker.getMasked(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getBody())));
                logger.info("###########################  #####################################");
            }
        } else if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            logger.info("No response body status code 204");
        } else {
            logger.info("message = Error occured");
        }
    }
}

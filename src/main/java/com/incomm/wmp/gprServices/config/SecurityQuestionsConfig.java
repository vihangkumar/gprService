package com.incomm.wmp.gprServices.config;

import com.incomm.wmp.gprServices.constants.Constants;
import com.incomm.wmp.gprServices.constants.UserEvents;
import com.incomm.wmp.gprServices.response.BaseResponse;
import com.incomm.wmp.gprServices.response.SercurityQuestionsResponse;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.omg.CORBA.portable.UnknownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import org.apache.logging.log4j.core.config.Order;

@ConfigurationProperties(prefix = "securityquestions")
@Configuration
@Order(1000)
@RestController
@RequestMapping("/gprServices")
public class SecurityQuestionsConfig extends WebSecurityConfigurerAdapter {

    protected Logger logger = LoggerFactory.getLogger(getClass());

 /*   Map<String,String> securityQuestions1 = new HashMap<String,String>();
    Map<String,String> securityQuestions2 = new HashMap<String,String>();
    Map<String,String> securityQuestions3 = new HashMap<String,String>();
*/
   //creating questions loading from properties file
    String question1;
    String question2;
    String question3;
    String question4;
    String question5;

    String question6;
    String question7;
    String question8;
    String question9;
    String question10;

    String question11;
    String question12;
    String question13;
    String question14;
    String question15;


    //setters and getters
    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getQuestion4() {
        return question4;
    }

    public void setQuestion4(String question4) {
        this.question4 = question4;
    }

    public String getQuestion5() {
        return question5;
    }

    public void setQuestion5(String question5) {
        this.question5 = question5;
    }

    public String getQuestion6() {
        return question6;
    }

    public void setQuestion6(String question6) {
        this.question6 = question6;
    }

    public String getQuestion7() {
        return question7;
    }

    public void setQuestion7(String question7) {
        this.question7 = question7;
    }

    public String getQuestion8() {
        return question8;
    }

    public void setQuestion8(String question8) {
        this.question8 = question8;
    }

    public String getQuestion9() {
        return question9;
    }

    public void setQuestion9(String question9) {
        this.question9 = question9;
    }

    public String getQuestion10() {
        return question10;
    }

    public void setQuestion10(String question10) {
        this.question10 = question10;
    }

    public String getQuestion11() {
        return question11;
    }

    public void setQuestion11(String question11) {
        this.question11 = question11;
    }

    public String getQuestion12() {
        return question12;
    }

    public void setQuestion12(String question12) {
        this.question12 = question12;
    }

    public String getQuestion13() {
        return question13;
    }

    public void setQuestion13(String question13) {
        this.question13 = question13;
    }

    public String getQuestion14() {
        return question14;
    }

    public void setQuestion14(String question14) {
        this.question14 = question14;
    }

    public String getQuestion15() {
        return question15;
    }

    public void setQuestion15(String question15) {
        this.question15 = question15;
    }

    //constructing the questions in sets
    @Bean(name = "securityQuestions1")
    @RequestMapping(value = "/loadSecurityQuestionsOne", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = SercurityQuestionsResponse.class)
    })
    public ResponseEntity<Object> getSecurityQuestionsMap1(){
        logger.info("message = entered into getSecurityQuestionsOne" + UserEvents.GETSECURITYQUESTIONSONE);
        SercurityQuestionsResponse securityQuestionsRespone = new SercurityQuestionsResponse();
       try{
           securityQuestionsRespone.setQuestion1(question1);
           securityQuestionsRespone.setQuestion2(question2);
           securityQuestionsRespone.setQuestion3(question3);
           securityQuestionsRespone.setQuestion4(question4);
           securityQuestionsRespone.setQuestion5(question5);
           return new ResponseEntity<>(securityQuestionsRespone, HttpStatus.OK);
       }
       catch (UnknownException e){
           logger.error("message = Internal error Occurred in GetSecurityQuestionOne, userEvent=" + UserEvents.GETSECURITYQUESTIONSONE);
           BaseResponse baseResponse = new BaseResponse();
           baseResponse.setRespCode(Constants.ERR_GET_SECURITY_QUESTIONS);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
       }

    }

    @Bean(name = "securityQuestions2")
    @RequestMapping(value = "/loadSecurityQuestionsTwo", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = SercurityQuestionsResponse.class)
    })
    public ResponseEntity<Object> getSecurityQuestionsMap2(){

        logger.info("message = entered into getSecurityQuestionsTwo" + UserEvents.GETSECURITYQUESTIONSTWO);
        try {


            SercurityQuestionsResponse securityQuestionsRespone = new SercurityQuestionsResponse();
            securityQuestionsRespone.setQuestion1(question6);
            securityQuestionsRespone.setQuestion2(question7);
            securityQuestionsRespone.setQuestion3(question8);
            securityQuestionsRespone.setQuestion4(question9);
            securityQuestionsRespone.setQuestion5(question10);
            return new ResponseEntity<>(securityQuestionsRespone, HttpStatus.OK);
        }catch (UnknownException e){
            logger.error("message = Internal error Occurred in GetSecurityQuestionOne, userEvent=" + UserEvents.GETSECURITYQUESTIONSONE);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_GET_SECURITY_QUESTIONS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }


    }

    @Bean(name = "securityQuestions3")
    @RequestMapping(value = "/loadSecurityQuestionsThree", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = SercurityQuestionsResponse.class)
    })
    public ResponseEntity<Object> getSecurityQuestionsMap3(){

        logger.info("message = entered into getSecurityQuestionsThree" + UserEvents.GETSECURITYQUESTIONSTHREE);
        try {
            SercurityQuestionsResponse securityQuestionsRespone = new SercurityQuestionsResponse();
            securityQuestionsRespone.setQuestion1(question11);
            securityQuestionsRespone.setQuestion2(question12);
            securityQuestionsRespone.setQuestion3(question13);
            securityQuestionsRespone.setQuestion4(question14);
            securityQuestionsRespone.setQuestion5(question15);
            return new ResponseEntity<>(securityQuestionsRespone, HttpStatus.OK);
        } catch (UnknownException e){
            logger.error("message = Internal error Occurred in GetSecurityQuestionOne, userEvent=" + UserEvents.GETSECURITYQUESTIONSONE);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_GET_SECURITY_QUESTIONS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }

    }
}



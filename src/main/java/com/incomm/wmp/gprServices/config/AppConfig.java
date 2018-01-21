package com.incomm.wmp.gprServices.config;

import com.incomm.wmp.gprServices.logging.LoggingInterceptor;
import com.incomm.wmp.gprServices.outbound.ingo.transport.http.IngoHttpsUrlConnectionMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@ConfigurationProperties(prefix = "gprServices")
@Configuration
public class AppConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    String chsEndpointUrl;
    String cardIssueEndpointUrl;
    String userManagementEndpointUrl;
    int wsConnectionTimeout;
    int wsReadTimeout;
    String ctxPath;

    String ingoCtxPath;
    int ingconnectTimeout;
    int ingoreadTimeout;
    boolean shouldVerifyHost;
    String ingoUrl;



    //Ingo Beans

    @Bean(name = "ingoMarshaller")
    public Jaxb2Marshaller getIngomarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(ingoCtxPath);
        return marshaller;
    }


    //@Value("${ingo_ws_connection_timeout}") int connectTimeout, @Value("${ingo_ws_read_timeout}") int readTimeout, @Value("${ingo_ws_verify_hostname}") Boolean shouldVerifyHost)

    /**
     *
     * @return
     */
    @Bean
    public IngoHttpsUrlConnectionMessageSender getIngoMessageSender() {
        IngoHttpsUrlConnectionMessageSender sender = new IngoHttpsUrlConnectionMessageSender();
        sender.setConnectionTimeout(ingconnectTimeout);
        sender.setReadTimeout(ingoreadTimeout);
        sender.setShouldVerifyHost(shouldVerifyHost);

        return sender;
    }

    @Bean  (name = "IngoWebServiceTemplate")
    public WebServiceTemplate getWebServiceTemplate(LoggingInterceptor loggingInterceptor, @Qualifier("ingoMarshaller")Jaxb2Marshaller ingoMarshaller, IngoHttpsUrlConnectionMessageSender ingoHttpUrlConnectionMessageSender) {

        WebServiceTemplate template = new WebServiceTemplate();
        template.setDefaultUri(ingoUrl);
        template.setMarshaller(ingoMarshaller);
        template.setUnmarshaller(ingoMarshaller);
        template.setInterceptors(new ClientInterceptor[]{loggingInterceptor});
        template.setMessageSender(ingoHttpUrlConnectionMessageSender);
        return template;
    }



    //VMS beans

    @Bean(name = "vmsMarshaller")
    public Jaxb2Marshaller getMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(ctxPath);
        return marshaller;
    }

    @Bean
    public LoggingInterceptor getLoggingInterceptor() {
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
        return loggingInterceptor;
    }


    @Bean
    public HttpComponentsMessageSender getHttpComponentsMessageSender() {
        HttpComponentsMessageSender sender = new HttpComponentsMessageSender();
        sender.setConnectionTimeout(wsConnectionTimeout);
        sender.setReadTimeout(wsReadTimeout);
        return sender;
    }


    @Bean(name = "usermanagementPort")
    public WebServiceTemplate getWebServiceTemplate2( @Qualifier("vmsMarshaller") Jaxb2Marshaller marshaller, LoggingInterceptor loggingInterceptor,
                                                     HttpComponentsMessageSender httpComponentsMessageSender) {
        WebServiceTemplate template = new WebServiceTemplate();
        template.setDefaultUri(userManagementEndpointUrl);
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        template.setInterceptors(new ClientInterceptor[]{loggingInterceptor});
        template.setMessageSender(httpComponentsMessageSender);
        return template;
    }


    @Bean(name = "cardIssuerPort")
    public WebServiceTemplate getWebServiceTemplate1(@Qualifier("vmsMarshaller") Jaxb2Marshaller marshaller, LoggingInterceptor loggingInterceptor,
                                                     HttpComponentsMessageSender httpComponentsMessageSender) {

        WebServiceTemplate template = new WebServiceTemplate();
        template.setDefaultUri(cardIssueEndpointUrl);
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        template.setInterceptors(new ClientInterceptor[]{loggingInterceptor});
        template.setMessageSender(httpComponentsMessageSender);
        return template;
    }


    @Bean(name = "chsPort")
    public WebServiceTemplate getWebServiceTemplate(@Qualifier("vmsMarshaller") Jaxb2Marshaller marshaller, LoggingInterceptor loggingInterceptor,
                                                    HttpComponentsMessageSender httpComponentsMessageSender) {

        WebServiceTemplate template = new WebServiceTemplate();
        template.setDefaultUri(chsEndpointUrl);
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        template.setInterceptors(new ClientInterceptor[]{loggingInterceptor});
        template.setMessageSender(httpComponentsMessageSender);
        return template;
    }

    public String getChsEndpointUrl() {
        return chsEndpointUrl;
    }

    public void setChsEndpointUrl(String chsEndpointUrl) {
        this.chsEndpointUrl = chsEndpointUrl;
    }

    public String getCardIssueEndpointUrl() {
        return cardIssueEndpointUrl;
    }

    public void setCardIssueEndpointUrl(String cardIssueEndpointUrl) {
        this.cardIssueEndpointUrl = cardIssueEndpointUrl;
    }

    public String getUserManagementEndpointUrl() {
        return userManagementEndpointUrl;
    }

    public void setUserManagementEndpointUrl(String userManagementEndpointUrl) {
        this.userManagementEndpointUrl = userManagementEndpointUrl;
    }

    public int getWsConnectionTimeout() {
        return wsConnectionTimeout;
    }

    public void setWsConnectionTimeout(int wsConnectionTimeout) {
        this.wsConnectionTimeout = wsConnectionTimeout;
    }

    public int getWsReadTimeout() {
        return wsReadTimeout;
    }

    public void setWsReadTimeout(int wsReadTimeout) {
        this.wsReadTimeout = wsReadTimeout;
    }

    public String getCtxPath() {
        return ctxPath;
    }

    public void setCtxPath(String ctxPath) {
        this.ctxPath = ctxPath;
    }
}

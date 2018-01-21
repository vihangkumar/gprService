package com.incomm.wmp.gprServices.logging;


import com.incomm.imp.neo.datamasking.xml.XmlPayloadMasker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.WebServiceTransformerException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpComponentsConnection;


import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dvontela on 8/8/2017.
 */
public class LoggingInterceptor implements ClientInterceptor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static AtomicInteger atomicInteger = new AtomicInteger();

    private static ThreadLocal<Integer> tracker = new ThreadLocal<Integer>();

    @Autowired
    XmlPayloadMasker masker;


    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        TransportContext transportContext = TransportContextHolder.getTransportContext();
        if (transportContext.getConnection() instanceof HttpComponentsConnection) {
            HttpComponentsConnection connection = (HttpComponentsConnection) transportContext.getConnection();
            tracker.set(atomicInteger.getAndIncrement());
            logger.info("connection " + connection.getHttpPost());
            transformSourceToString(messageContext.getRequest().getPayloadSource());
        }
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        transformSourceToString(messageContext.getResponse().getPayloadSource());
        TransportContext transportContext = TransportContextHolder.getTransportContext();
        if (transportContext.getConnection() instanceof HttpComponentsConnection) {
            HttpComponentsConnection connection = (HttpComponentsConnection) transportContext.getConnection();
        }
        tracker.remove();
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        return false;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception e) throws WebServiceClientException {

    }

    private void transformSourceToString(Source source) {

        String sourceString;
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StringWriter outWriter = new StringWriter();
            StreamResult result = new StreamResult(outWriter);
            transformer.transform(source, result);
            StringBuffer sb = outWriter.getBuffer();
            sourceString = sb.toString();
            logger.info("Soap Payload" + masker.getMasked(sourceString));

        } catch (TransformerException transformerException) {
            throw new WebServiceTransformerException(
                    "Could not transform source",
                    transformerException);
        }
    }
}
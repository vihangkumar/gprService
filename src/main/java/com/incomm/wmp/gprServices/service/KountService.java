package com.incomm.wmp.gprServices.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.wmp.gprServices.service.model.DeviceIdResponse;
import com.incomm.wmp.gprServices.util.VmsServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

//import org.auth.cardmanagement.*;
import com.incomm.chstypes.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.*;

@Service
@ConfigurationProperties(prefix = "chs")
public class KountService {

    private static final Logger logger = LoggerFactory.getLogger(KountService.class);

    private String kountApiKey;
    private String kountVersion;
    private String kountMerchantId;
    private String kountServiceUrl;
    private int kountReadTimeout;
    private int kountConnectionTimeout;
    private Boolean enableKount;

    private RestTemplate restTemplate;

    @Autowired
    VmsServiceUtil vmsServiceUtil;

     public DeviceIdResponse getDeviceDetails(String kountSessionId) {
        DeviceIdResponse response = null;

        if (this.enableKount != null && this.enableKount) {
            if (kountSessionId == null || kountSessionId.isEmpty()) {
                throw new IllegalArgumentException("The Kount Session id must not be null or empty");
            }

            try {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(new URI(this.kountServiceUrl))
                        .queryParam("s", kountSessionId);

                if (this.kountVersion != null && !this.kountVersion.isEmpty()) {
                    uriComponentsBuilder.queryParam("v", this.kountVersion);
                }

                // KountAccess does not respect Accept header.  text is always returned. It needs to be converted to object manually.
                String stringResponse = restTemplate.getForObject(uriComponentsBuilder.build().encode().toUri(), String.class);
                ObjectMapper om = new ObjectMapper();

                response = om.readValue(stringResponse, DeviceIdResponse.class);
                logger.info("Device Id returned for session id [" + kountSessionId + "]: " + response.toString());
            } catch (HttpClientErrorException e) {
                logger.warn("Exception while retrieving device id with session id [" + kountSessionId + "]: " + e.getMessage());
                throw new IllegalArgumentException(e);
            } catch (Exception e) {
                logger.error("Exception while retrieving device id with session id [" + kountSessionId + "]: ", e);
            }
        }
        return response;
    }

    public Header getCHSKountHeader(String username, String srcAppId, HttpServletRequest httpServletRequest, String kountSessionId) throws UnknownHostException {

        Header chsHeader = vmsServiceUtil.getCHSHeader(username,srcAppId,httpServletRequest);
        DeviceIdResponse did = getDeviceDetails(kountSessionId);

        if (did != null && did.getDevice() != null) {
            VelocityRequest velocityRequest = new VelocityRequest();
            velocityRequest.setDeviceCountry(did.getDevice().getCountry());
            velocityRequest.setDeviceId(did.getDevice().getId());
            velocityRequest.setDeviceLat(did.getDevice().getGeoLat());
            velocityRequest.setDeviceLong(did.getDevice().getGeoLong());
            velocityRequest.setDeviceRegion(did.getDevice().getRegion());
            velocityRequest.setIpAddress(did.getDevice().getIpAddress());
            velocityRequest.setIpCountry(did.getDevice().getIpGeo());
            velocityRequest.setMobile(did.getDevice().getMobile());
            velocityRequest.setProxy(did.getDevice().getProxy());
            velocityRequest.setSessionId(kountSessionId);

            chsHeader.setVelocityRequest(velocityRequest);
        }

        return chsHeader;
    }

    @PostConstruct
    public void initializeRestTemplate() {
        restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(factory));
        factory.setConnectTimeout(this.kountConnectionTimeout);
        factory.setReadTimeout(this.kountReadTimeout);
        restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(this.kountMerchantId, this.kountApiKey));
    }

    public String getKountApiKey() {
        return kountApiKey;
    }

    public void setKountApiKey(String kountApiKey) {
        this.kountApiKey = kountApiKey;
    }

    public String getKountVersion() {
        return kountVersion;
    }

    public void setKountVersion(String kountVersion) {
        this.kountVersion = kountVersion;
    }

    public String getKountMerchantId() {
        return kountMerchantId;
    }

    public void setKountMerchantId(String kountMerchantId) {
        this.kountMerchantId = kountMerchantId;
    }

    public String getKountServiceUrl() {
        return kountServiceUrl;
    }

    public void setKountServiceUrl(String kountServiceUrl) {
        this.kountServiceUrl = kountServiceUrl;
    }

    public int getKountReadTimeout() {
        return kountReadTimeout;
    }

    public void setKountReadTimeout(int kountReadTimeout) {
        this.kountReadTimeout = kountReadTimeout;
    }

    public int getKountConnectionTimeout() {
        return kountConnectionTimeout;
    }

    public void setKountConnectionTimeout(int kountConnectionTimeout) {
        this.kountConnectionTimeout = kountConnectionTimeout;
    }

    public Boolean getEnableKount() {
        return enableKount;
    }

    public void setEnableKount(Boolean enableKount) {
        this.enableKount = enableKount;
    }
}

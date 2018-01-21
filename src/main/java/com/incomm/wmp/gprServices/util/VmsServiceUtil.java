package com.incomm.wmp.gprServices.util;

//import org.auth.cardmanagement.Header;
import com.incomm.chstypes.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dvontela on 8/9/2017.
 */
@Component
public class VmsServiceUtil {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public Header getCHSHeader(String username, String srcAppId, HttpServletRequest httpServletRequest) throws UnknownHostException {

        Header chsHeader = new Header();
        chsHeader.setSrcAppId(srcAppId);
        chsHeader.setSrcIpAddress(getRemoteIPAddr(httpServletRequest));
        chsHeader.setWebIpAddress(InetAddress.getLocalHost().getHostAddress());
        chsHeader.setUserName(username);

        return chsHeader;
    }
    public String getRemoteIPAddr(HttpServletRequest request)  throws UnknownHostException {
        boolean isXForwardFor = true;

        String remoteHost = request.getHeader("X-Forwarded-For");

        if (remoteHost == null || remoteHost.length() == 0) {
            remoteHost = InetAddress.getLocalHost().getHostAddress();
            isXForwardFor = false;
        }

        // Check and format ip
        if (remoteHost != null) {
            remoteHost = remoteHost.trim();
            int i = remoteHost.indexOf(',');
            if (i > 0)
                remoteHost = remoteHost.substring(0, i);
        } else {
            //TODO we need to change this to request.getRemoteAddr() once VMS can handle both IPv6 and IPv4
            remoteHost = InetAddress.getLocalHost().getHostAddress();
            if(remoteHost == null)
                remoteHost = "unknown";
        }

        // logging X-Forwarded-For value
        if (isXForwardFor)
            logger.info("X-Forwarded-For is: " + remoteHost);
        else
            logger.info("As X-Forwarded-For is null or length is 0, get RemoteHost instead: " + remoteHost);

        return remoteHost;
    }
}

package com.incomm.vms.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Created by dvontela on 6/16/2017.
 */

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

    public SecurityInitializer() {
        super(SecurityConfig.class, HazelcastHttpClientConfig.class);
    }

}


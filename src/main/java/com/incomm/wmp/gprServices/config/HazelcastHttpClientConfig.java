package com.incomm.wmp.gprServices.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import java.util.List;

/**
 * Created by dvontela on 7/20/2017.
 */
@EnableHazelcastHttpSession(maxInactiveIntervalInSeconds = 900, sessionMapName = "spring:session:VMSSESSIONS")
@Configuration
@ConfigurationProperties(prefix = "hazelcast")
public class HazelcastHttpClientConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private List<String> networkAddress;
    private String groupName;
    private String groupPassword;
    private String nearcacheName;
    private int evictionSize;
    private int nearcacheIdletimeout;
    private int nearcacheTimetolive;

    @Bean
    public HazelcastInstance hazelcastInstance() {

        logger.info("message = Creating the hazelcast client instance");
        MapConfig binMap = new MapConfig();

        EvictionConfig evictionConfig = new EvictionConfig();
        evictionConfig.setMaximumSizePolicy(EvictionConfig.MaxSizePolicy.ENTRY_COUNT)
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setSize(evictionSize);

        logger.info("message = configuring hazelcast client near cache");
        NearCacheConfig nearCacheConfig = new NearCacheConfig();
        nearCacheConfig.setName(nearcacheName)
                .setTimeToLiveSeconds(nearcacheTimetolive)
                .setEvictionConfig(evictionConfig)
                .setMaxIdleSeconds(nearcacheIdletimeout)
                .setCacheLocalEntries(true)
                .setLocalUpdatePolicy(NearCacheConfig.LocalUpdatePolicy.CACHE);

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getGroupConfig().setName(groupName).setPassword(groupPassword);
        clientConfig.getNetworkConfig().setAddresses(networkAddress);
        clientConfig.addNearCacheConfig(nearCacheConfig);

        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        logger.info("message = hazelcast client instance created");
        return hazelcastInstance;
    }

    @Bean
    HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }

    public List<String> getNetworkAddress() {
        return networkAddress;
    }

    public void setNetworkAddress(List<String> networkAddress) {
        this.networkAddress = networkAddress;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    public void setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
    }

    public int getEvictionSize() {
        return evictionSize;
    }

    public void setEvictionSize(int evictionSize) {
        this.evictionSize = evictionSize;
    }

    public int getNearcacheIdletimeout() {
        return nearcacheIdletimeout;
    }

    public void setNearcacheIdletimeout(int nearcacheIdletimeout) {
        this.nearcacheIdletimeout = nearcacheIdletimeout;
    }

    public int getNearcacheTimetolive() {
        return nearcacheTimetolive;
    }

    public void setNearcacheTimetolive(int nearcacheTimetolive) {
        this.nearcacheTimetolive = nearcacheTimetolive;
    }

    public String getNearcacheName() {
        return nearcacheName;
    }

    public void setNearcacheName(String nearcacheName) {
        this.nearcacheName = nearcacheName;
    }
}
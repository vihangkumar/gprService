package com.incomm.wmp.gprServices.config;

import com.incomm.imp.neo.datamasking.config.MaskingConfiguration;
import com.incomm.imp.neo.datamasking.json.JsonPayloadMasker;
import com.incomm.imp.neo.datamasking.xml.XmlPayloadMasker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * Created by dvontela on 7/18/2017.
 */

@Configuration
public class MaskingConfigurationVMS {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String VMS_MASKING_PROPERTIES = "masking.properties";
    private static final String SYS_VMS_PROPERTY_PATH = "VMS_PROPERTIES_FILE_PATH";
    private static final String SYS_VMS_MASKING_PROPERTY_PATH = "VMS_MASKING_FILE_PATH";

    @Bean
    public XmlPayloadMasker getXmlPayloadMasker(){
        String maskingConfigString = buildMaskingConfigString(getMaskingProperties());
        MaskingConfiguration maskingConfiguration = new MaskingConfiguration(maskingConfigString);
        XmlPayloadMasker masker = new XmlPayloadMasker(maskingConfiguration);
        return masker;
    }

    @Bean
    public JsonPayloadMasker getJsonPayloadMasker() {
        String maskingConfigString = buildMaskingConfigString(getMaskingProperties());
        MaskingConfiguration maskingConfiguration = new MaskingConfiguration(maskingConfigString);
        JsonPayloadMasker masker = new JsonPayloadMasker(maskingConfiguration);
        return masker;
    }
    private String buildMaskingConfigString(Properties properties) {

        StringBuilder sb = new StringBuilder();
        Set propertyKeys = properties.keySet();

        for (Object key : propertyKeys) {
            String currentKey = (String) key;
            sb.append(currentKey);
            sb.append(":");
            sb.append(properties.getProperty(currentKey));
            sb.append("|");
        }
        sb.deleteCharAt(sb.lastIndexOf("|"));
        return new String(sb);
    }

    private Properties getMaskingProperties() {

        Properties properties = new Properties();
        Resource resource = null;
        String propertyPath = System.getProperty(SYS_VMS_MASKING_PROPERTY_PATH);

        if (propertyPath != null && propertyPath.length() != 0) {
            logger.info("System property '" + SYS_VMS_PROPERTY_PATH + "' configured.  Using the path [" + propertyPath + "].");
            resource = new FileSystemResource(propertyPath);
            try {
                properties.load(resource.getInputStream());
            } catch (IOException e) {
            }
        } else {
            logger.warn("No system property '" + SYS_VMS_PROPERTY_PATH + "' configured.  Using default settings from classpath. [classpath:" + VMS_MASKING_PROPERTIES + "]");
            resource = new ClassPathResource(VMS_MASKING_PROPERTIES);
            try {
                properties.load(resource.getInputStream());
            } catch (IOException e) {
            }
        }
        return properties;
    }

}

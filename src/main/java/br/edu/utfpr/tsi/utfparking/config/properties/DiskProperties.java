package br.edu.utfpr.tsi.utfparking.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "files")
public class DiskProperties {
    private String root;

    private String avatar;
}

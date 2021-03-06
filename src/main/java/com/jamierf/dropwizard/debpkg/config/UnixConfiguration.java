package com.jamierf.dropwizard.debpkg.config;

import org.apache.maven.plugins.annotations.Parameter;

public class UnixConfiguration {

    @Parameter
    private String user = "dropwizard";

    public String getUser() {
        return user;
    }

    public UnixConfiguration setUser(final String user) {
        this.user = user;
        return this;
    }
}

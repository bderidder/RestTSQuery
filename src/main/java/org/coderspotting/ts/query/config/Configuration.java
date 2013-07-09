package org.coderspotting.ts.query.config;

public interface Configuration
{
    public String getProperty(String key);
    public String getProperty(String key, String defaulValue);
}

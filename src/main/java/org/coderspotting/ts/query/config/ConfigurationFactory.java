package org.coderspotting.ts.query.config;

import org.coderspotting.ts.query.config.rt.PropertiesConfiguration;

public class ConfigurationFactory
{
    private static Configuration configuration = null;
    
    public ConfigurationFactory()
    {
        if (configuration == null)
        {
            loadConfiguration();
        }
    }
    
    public final Configuration getConfiguration()
    {
        return configuration;
    }
    
    private void loadConfiguration()
    {
        configuration = new PropertiesConfiguration();
    }
}

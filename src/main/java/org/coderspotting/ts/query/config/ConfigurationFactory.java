package org.coderspotting.ts.query.config;

import org.coderspotting.ts.query.config.rt.PropertiesConfiguration;

/**
 * A factory class to fetch an instance of a Configuration
 *
 * @author Bavo De Ridder <http://www.coderspotting.org/>
 */
public class ConfigurationFactory
{
    private static Configuration configuration = null;

    /**
     * Fetch an instance of a Configuration. Subsequent calls to this method will always return the same instance.
     *
     * @return singleton instance of a Configuration
     */
    public static Configuration getConfiguration()
    {
        if (configuration == null)
        {
            configuration = new PropertiesConfiguration();
        }

        return configuration;
    }
}

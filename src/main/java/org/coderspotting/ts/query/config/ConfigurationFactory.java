package org.coderspotting.ts.query.config;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * A factory class to fetch an instance of a Configuration
 *
 * @author Bavo De Ridder <http://www.coderspotting.org/>
 */
public class ConfigurationFactory
{
    private static final Logger logger = Logger.getLogger(ConfigurationFactory.class.getName());
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
            try
            {
                configuration = new PropertiesConfiguration("config.properties");
            }
            catch (ConfigurationException ex)
            {
                logger.log(Level.SEVERE, null, ex);

                logger.log(Level.FINE, "Could not create configuration from properties, creating empty configuration");
                configuration = new PropertiesConfiguration();
            }
        }

        return configuration;
    }
}

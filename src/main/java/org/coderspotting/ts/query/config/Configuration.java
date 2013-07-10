package org.coderspotting.ts.query.config;

/**
 * A simple interface to get configuration properties.
 *
 * @author Bavo De Ridder <http://www.coderspotting.org/>
 */
public interface Configuration
{
    /**
     * Get the value for a configuration property.
     * 
     * @param key the name of the property
     * @return the value if that property exists or null otherwise
     */
    public String getProperty(String key);
    
    /**
     * Get the value for a configuration property. If the property does
     * not exist, return the default value.
     * 
     * @param key the name of the property
     * @param defaulValue the default value to return if that property doesn't exist
     * @return the value if that property exists or the default value otherwise
     */
    public String getProperty(String key, String defaulValue);
}

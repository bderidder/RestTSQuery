package org.coderspotting.ts.query.config.rt;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.coderspotting.ts.query.config.Configuration;
import org.coderspotting.ts.query.rest.ServerQuery;

public class PropertiesConfiguration implements Configuration
{
    private final static Logger LOG = Logger.getLogger(ServerQuery.class.getName());
    private Properties properties;

    public PropertiesConfiguration()
    {
        loadFromFileOnClasspath();
    }

    @Override
    public String getProperty(String key)
    {
        return properties.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaulValue)
    {
        return properties.getProperty(key, defaulValue);
    }

    private void loadFromFileOnClasspath()
    {
        properties = new Properties();

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.properties");

        try (Reader reader = new InputStreamReader(in, "UTF-8"))
        {
            properties.load(reader);
            
            in.close();
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, "Could not read configuration file, resorting to empty configuration", e);
        }
    }
}

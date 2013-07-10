package org.coderspotting.ts.query.cache;

import org.coderspotting.ts.query.cache.rt.CacheImpl;

/**
 * A factory class to fetch an instance of a Cache
 *
 * @author Bavo De Ridder <http://www.coderspotting.org/>
 */
public class CacheFactory
{
    private static Cache cache = null;

    /**
     * Fetch an instance of a Cache. Subsequent calls to this method
     * will always return the same instance.
     * 
     * @return singleton instance of a Cache
     */
    public static Cache getCache()
    {
        if (cache == null)
        {
            cache = new CacheImpl();
        }

        return cache;
    }
}

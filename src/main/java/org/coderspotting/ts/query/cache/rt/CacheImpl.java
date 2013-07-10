package org.coderspotting.ts.query.cache.rt;

import java.util.HashMap;
import org.coderspotting.ts.query.cache.Cache;

/**
 * Simple implementation of the Cache interface based on an in-memory HashMap.
 * 
 * Entries are not automatically purged when their time-to-live expires. We
 * only purge when items are requested.
 *
 * @author Bavo De Ridder <http://www.coderspotting.org/>
 */
public class CacheImpl implements Cache
{
    private HashMap<String, CacheEntry> cacheEntries;

    public CacheImpl()
    {
        cacheEntries = new HashMap<>();
    }

    @Override
    public void putEntry(String key, Object entry, long ttl)
    {
        if (cacheEntries.containsKey(key))
        {
            cacheEntries.remove(key);
        }

        long currentTime = System.currentTimeMillis();

        CacheEntry newEntry = new CacheEntry(entry, currentTime + ttl);

        cacheEntries.put(key, newEntry);
    }

    @Override
    public Object getEntry(String key)
    {
        CacheEntry entry = cacheEntries.get(key);

        if (entry == null)
        {
            return null;
        }

        long currentTime = System.currentTimeMillis();

        if (currentTime > entry.getTtl())
        {
            cacheEntries.remove(key);
            return null;
        }

        return entry.getValue();
    }
}

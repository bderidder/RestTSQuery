package org.coderspotting.ts.query.cache.spi;

import java.util.HashMap;
import org.coderspotting.ts.query.cache.Cache;

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

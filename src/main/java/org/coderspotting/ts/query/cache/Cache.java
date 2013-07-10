package org.coderspotting.ts.query.cache;

/**
 * An simple interface to add and fetch items from a cache.
 *
 * @author Bavo De Ridder <http://www.coderspotting.org/>
 */
public interface Cache
{
    /**
     * Add an entry to the cache. If an entry with the given name already exists
     * it is removed first.
     * 
     * @param key the name of the entry in the cache
     * @param entry the object to cache
     * @param ttl the time-to-live for the entry, expressed in milliseconds
     */
    public void putEntry(String key, Object entry, long ttl);

    /**
     * Return the entry in the cache with the given name. If no such entry exists, null
     * is returned. If such an entry exists but the time-to-live has expired, null
     * is returned.
     * 
     * @param key the name of the entry in the cache
     * @return
     */
    public Object getEntry(String key);
}

package org.coderspotting.ts.query.cache.rt;

/**
 * A helper class to store the cached object together with the time-to-live.
 *
 * @author Bavo De Ridder <http://www.coderspotting.org/>
 */
public final class CacheEntry
{
    private Object value;
    private long ttl;

    public CacheEntry(Object value, long ttl)
    {
        setValue(value);
        setTtl(ttl);
    }

    public final Object getValue()
    {
        return value;
    }

    private void setValue(Object value)
    {
        this.value = value;
    }

    public final long getTtl()
    {
        return ttl;
    }

    private void setTtl(long ttl)
    {
        this.ttl = ttl;
    }
}

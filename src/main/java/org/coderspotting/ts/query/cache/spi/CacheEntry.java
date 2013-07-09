/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.coderspotting.ts.query.cache.spi;

/**
 *
 * @author Bavo
 */
public class CacheEntry
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

    private final void setValue(Object value)
    {
        this.value = value;
    }

    public final long getTtl()
    {
        return ttl;
    }

    private final void setTtl(long ttl)
    {
        this.ttl = ttl;
    }
}

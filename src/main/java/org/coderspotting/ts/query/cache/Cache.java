/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.coderspotting.ts.query.cache;

/**
 *
 * @author Bavo
 */
public interface Cache
{
    public void putEntry(String key, Object entry, long ttl);

    public Object getEntry(String key);
}

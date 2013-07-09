/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.coderspotting.ts.query.cache;

import org.coderspotting.ts.query.cache.spi.CacheImpl;

/**
 *
 * @author Bavo
 */
public class CacheFactory
{
    private static Cache cache = null;

    public static Cache getCache()
    {
        if (cache == null)
        {
            cache = new CacheImpl();
        }

        return cache;
    }
}

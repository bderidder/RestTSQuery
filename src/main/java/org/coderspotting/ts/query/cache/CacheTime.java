package org.coderspotting.ts.query.cache;

/**
 * Useful time constants to express Time To Live values
 * expressed in milliseconds.
 *
 * @author Bavo De Ridder <http://www.coderspotting.org/>
 */
public class CacheTime
{
    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long WEEK = 7 * DAY;
}

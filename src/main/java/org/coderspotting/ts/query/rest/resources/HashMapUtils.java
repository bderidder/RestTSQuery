package org.coderspotting.ts.query.rest.resources;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class HashMapUtils
{
    public static void outputHashMap(HashMap<String, String> hashMap)
    {
        if (hashMap == null)
        {
            return;
        }

        Collection<String> cValue = hashMap.values();
        Collection<String> cKey = hashMap.keySet();
        Iterator<String> itrValue = cValue.iterator();
        Iterator<String> itrKey = cKey.iterator();

        while (itrValue.hasNext() && itrKey.hasNext())
        {
            System.out.println(itrKey.next() + ": " + itrValue.next());
        }
    }

    public static void outputHashMap(List<HashMap<String, String>> hashMapList)
    {
        if (hashMapList == null)
        {
            return;
        }

        for(HashMap<String, String> hashMap : hashMapList)
        {
            outputHashMap(hashMap);
        }
    }
}

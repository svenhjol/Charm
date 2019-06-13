package svenhjol.meson.asm;

import java.util.TreeMap;

public class ClassNameMap extends TreeMap<String, String>
{
    public ClassNameMap(String... s)
    {
        for(int i = 0; i < s.length / 2; i++)
        {
            put(s[i * 2], s[i * 2 + 1]);
        }
    }

    @Override
    public String put(String key, String value)
    {
        return super.put("L" + key + ";", "L" + value + ";");
    }
}
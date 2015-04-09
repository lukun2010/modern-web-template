package controllers;

/**
 * Created by lu.kun on 2015/4/8.
 */

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class KafkaSourceContext {

    private Map<String, String> parameters;

    public KafkaSourceContext() {
        parameters = Collections.synchronizedMap(new HashMap<String, String>());
    }

    public ImmutableMap<String, String> getParameters() {
        synchronized (parameters) {
            return ImmutableMap.copyOf(parameters);
        }
    }

    public void clear() {
        parameters.clear();
    }

    public ImmutableMap<String, String> getSubProperties(String prefix) {
        Preconditions.checkArgument(prefix.endsWith("."),
                "The given prefix does not end with a period (" + prefix + ")");
        Map<String, String> result = Maps.newHashMap();
        synchronized(parameters) {
            for (String key : parameters.keySet()) {
                if (key.startsWith(prefix)) {
                    String name = key.substring(prefix.length());
                    result.put(name, parameters.get(key));
                }
            }
        }
        return ImmutableMap.copyOf(result);
    }

    public String getString(String key, String defaultValue) {
        return get(key, defaultValue);
    }
    /**
     * Gets value mapped to key, returning null if unmapped.
     * @param key to be found
     * @return value associated with key or null if unmapped
     */
    public String getString(String key) {
        return get(key);
    }
    private String get(String key, String defaultValue) {
        String result = parameters.get(key);
        if (result != null) {
            return result;
        }
        return defaultValue;
    }

    private String get(String key) {
        return get(key, null);
    }

    public String toString() {
        return "{ parameters:" + parameters + " }";
    }

}

package com.example.rednote.common.utils;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalUtils {

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

    public static void set(String key, Object value) {
        THREAD_LOCAL.get().put(key, value);
    }

    public static <T> T get(String key) {
        return (T) THREAD_LOCAL.get().get(key);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}

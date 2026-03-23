package com.example.rednote.common.utils;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalUtils {

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

    public static void set(String key, Object value) {
        log.debug("ThreadLocal 设置 {} = {}", key, value);
        THREAD_LOCAL.get().put(key, value);
    }

    public static <T> T get(String key) {
        T value = (T) THREAD_LOCAL.get().get(key);
        log.debug("从 ThreadLocal 获取 {} = {}", key, value);
        return value;
    }

    public static void remove() {
        log.debug("清除 ThreadLocal 数据");
        THREAD_LOCAL.remove();
    }
}

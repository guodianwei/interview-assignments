package com.example.shorturl.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class BeanCopierUtils {

    private static final ConcurrentHashMap<String, BeanCopier> cacheMap = new ConcurrentHashMap<>();

    public static <O, T> T convert(O source, Class<T> target) {
        return baseConvert(source, target);
    }

    public static <O, T> T convert(O source, T target) {
        return baseConvert(source, target);
    }

    private static <O, T> T baseConvert(O source, Class<T> target) {
        String baseKey = generateKey(source.getClass(), target);
        BeanCopier beanCopier;
        if (!cacheMap.containsKey(baseKey)) {
            beanCopier = BeanCopier.create(source.getClass(), target, false);
            cacheMap.put(baseKey, beanCopier);
        } else {
            beanCopier = cacheMap.get(baseKey);
        }
        T instance = null;
        try {
            instance = target.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            log.error(" mapper create bean error " + e.getMessage());
        }
        beanCopier.copy(source, instance, null);
        return instance;
    }

    private static <O, T> T baseConvert(O source, T target) {
        String baseKey = generateKey(source.getClass(), target.getClass());
        BeanCopier beanCopier;
        if (!cacheMap.containsKey(baseKey)) {
            beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
            cacheMap.put(baseKey, beanCopier);
        } else {
            beanCopier = cacheMap.get(baseKey);
        }
        beanCopier.copy(source, target, null);
        return target;
    }

    private static <T> String generateKey(Class<?> aClass, Class<T> bClass) {
        return (aClass == null ? "" : aClass.toString()) + (bClass == null ? "" : bClass.toString());
    }

}

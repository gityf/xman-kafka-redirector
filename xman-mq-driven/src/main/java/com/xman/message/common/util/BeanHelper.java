package com.xman.message.common.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BeanHelper {
    public static Map<String, Object> convertBeanToMap(Object bean)
            throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = bean.getClass().getDeclaredFields();
        HashMap<String, Object> data = new HashMap<String, Object>();
        for (Field field : fields) {
            field.setAccessible(true);
            data.put(field.getName(), field.get(bean));
        }
        return data;
    }
}
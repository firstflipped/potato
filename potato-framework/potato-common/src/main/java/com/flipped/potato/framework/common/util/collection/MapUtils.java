package com.flipped.potato.framework.common.util.collection;

import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * map工具类
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2023-09-21 14:29:25
 */
public class MapUtils {

    /**
     * 实体转Map
     *
     * @param bean 实体
     * @return Map
     */
    public static <T> Map<String, String> bean2Map(T bean) {
        Map<String, String> map = new HashMap<>(8);
        for (Field field : bean.getClass().getDeclaredFields()) {
            try {
                boolean flag = field.canAccess(bean);
                ;
                field.setAccessible(true);
                String value = field.get(bean).toString();
                map.put(field.getName(), value);
                field.setAccessible(flag);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return map;
    }

    /**
     * Map转实体
     *
     * @param map   map
     * @param clazz JavaBean类型
     * @return 泛型对象
     */
    public static <T> T map2Bean(Map<String, String> map, Class<T> clazz) {
        try {
            T t = clazz.getDeclaredConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                if (map.containsKey(field.getName())) {
                    boolean flag = field.canAccess(t);
                    field.setAccessible(true);
                    Object object = map.get(field.getName());
                    if (object != null && field.getType().isAssignableFrom(object.getClass())) {
                        field.set(t, object);
                    }
                    field.setAccessible(flag);
                }
            }
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 驼峰转下划线
     *
     * @param map map
     * @return map
     */
    public static Map<String, String> humpToUnderline(Map<String, String> map) {
        Map<String, String> transitionMap = new HashMap<>(16);
        map.forEach((k, v) -> transitionMap.put(StrUtil.toUnderlineCase(k), v));
        return transitionMap;
    }


}

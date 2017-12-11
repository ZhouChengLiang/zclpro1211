package org.zclpro.common.redistool;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Pojo2Map {

    private Pojo2Map() {
    }

    /**
     * <p>将对象转换为Map<String, String>
     * <p>此方法只转换对象属性上有此注解的{@link AddHash}
     *
     * @param obj
     * @return
     * @throws RedisOperationException 转换异常时抛出，程序中不用捕捉
     */
    public static Map<String, String> toMap(Object obj) {
        Field[] fields = getAllParentFiled(obj.getClass());
        Map<String, String> result = new HashMap<String, String>();
        for (Field aField : fields) {
            if (aField.getAnnotation(AddHash.class) == null) {
                continue;
            }
            putFieldValueToMap(aField, obj, result);
        }

        return result;
    }

    private static Field[] getAllParentFiled(Class<?> c) {
        List<Field> allFields = new ArrayList<>();
        for (; c != Object.class; c = c.getSuperclass()) {
            try {
                Field[] fields = c.getDeclaredFields();
                allFields.addAll(Arrays.asList(fields));
            } catch (Exception e) {

            }
        }
        Field[] fs = new Field[allFields.size()];
        allFields.toArray(fs);
        return fs;
    }


    private static void putFieldValueToMap(Field aField, Object obj, Map<String, String> map) {
        aField.setAccessible(true);
        try {
            Object aValue = aField.get(obj);
            if (aValue != null) {
                if (aValue instanceof String) {
                    map.put(aField.getName(), (String) aValue);
                } else if (aValue instanceof Number) {
                    map.put(aField.getName(), aValue.toString());
                } else if (aValue instanceof Date) {
                    map.put(aField.getName(), String.valueOf(((Date) aValue).getTime()));
                }
            }
        } catch (Exception e) {
            throw new RedisOperationException();
        }
    }

    /**
     * <p>将对象转换为Map<String, String>
     * <p>此方法转换对象属性上无此注解的{@link RedisHashIgnore}
     *
     * @param obj
     * @return
     * @throws RedisOperationException 转换异常时抛出，程序中不用捕捉
     */
    static Map<String, String> toMapWithoutIgnore(Object obj) {
        Field[] fields = FieldUtils.getAllFields(obj.getClass());
        Map<String, String> result = new HashMap<String, String>();
        for (Field aField : fields) {
            if (aField.getAnnotation(RedisHashIgnore.class) != null) {
                continue;
            }
            putFieldValueToMap(aField, obj, result);
        }

        return result;
    }

    public static <T> T convert2Object(Map<String, String> hm, Class<T> class1) {
        try {
            T obj = class1.newInstance();
            Field[] fields = getAllParentFiled(class1);
            for (Field f : fields) {
                Class<?> typeClass = f.getType();
                String value = hm.get(f.getName());
                if (value == null || Modifier.isFinal(f.getModifiers())) {
                    continue;
                }
                f.setAccessible(true);
                if (typeClass.equals(String.class)) {
                    f.set(obj, value);
                } else if (typeClass.equals(Long.class) || typeClass.equals(long.class)) {
                    f.set(obj, Long.parseLong(value));
                } else if (typeClass.equals(Integer.class) || typeClass.equals(int.class)) {
                    f.set(obj, Integer.parseInt(value));
                } else if (typeClass.equals(Double.class) || typeClass.equals(double.class)) {
                    f.set(obj, Double.parseDouble(value));
                } else if (typeClass.equals(Float.class) || typeClass.equals(float.class)) {
                    f.set(obj, Float.parseFloat(value));
                } else if (typeClass.equals(boolean.class) || typeClass.equals(Boolean.class)) {
                    f.set(obj, Boolean.parseBoolean(value));
                } else if (typeClass.equals(Date.class)) {
                    f.set(obj, (new Date(Long.parseLong(value))));
                } else {
                    throw new RuntimeException("不支持的数据类型" + typeClass.getName());
                }
                f.setAccessible(false);
            }
            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("不合法的类型：" + class1.getName(), e);
        }
    }
}

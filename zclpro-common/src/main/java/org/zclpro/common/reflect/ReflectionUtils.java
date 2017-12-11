package org.zclpro.common.reflect;

import java.lang.reflect.Field;

public class ReflectionUtils {

	/**
	 * 循环向上转型, 获取对象的 DeclaredField 
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException 
	 */
    private static Field getDeclaredField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field;
          
        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {  
            try {  
                field = clazz.getDeclaredField(fieldName) ;  
                return field ;  
            } catch (Exception e) {  
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。  
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了  
            }   
        }  
      
        throw new NoSuchFieldException(fieldName);  
    }     
    
    /** 
     * 循环向上转型, 获取对象的 DeclaredField 
     * @param object : 子类对象 
     * @param fieldName : 父类中的属性名 
     * @return 父类中的属性对象 
     * @throws NoSuchFieldException 
     */  
      
    public static Field getDeclaredField(Object obj, String fieldName) throws NoSuchFieldException{  
       return getDeclaredField(obj.getClass(), fieldName);
    }     
}
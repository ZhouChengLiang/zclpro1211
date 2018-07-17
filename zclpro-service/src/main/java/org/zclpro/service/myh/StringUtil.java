package org.zclpro.service.myh;

import java.text.MessageFormat;


public class StringUtil {
    
    public static final String spilt_char = "!@#";

    public static final String show_char = "  |  ";
    
    public static final String spilt_and_char = "&&&";
    
    public static final String spilt_arrow = " > ";

    /**
     * 替换key字符串里面的占位符字段
     * 
     * @param key
     * @param args
     * @return
     */
    public static String replaceStr(String key,Object... args){
        String str=MessageFormat.format(key, args);
        return str;
        
    }
    
}

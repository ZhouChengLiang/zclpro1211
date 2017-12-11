package org.zclpro.common.propertiestool;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.ImmutableSet;

public class StringManager {

    private static final Logger log = LoggerFactory.getLogger(StringManager.class);

    private Properties properties;

    private static final Map<String, StringManager> cache = new ConcurrentHashMap<>();

    private Set<String> keySet;

    /**
     * 返回key集合(不可变)
     *
     * @return
     */
    public Set<String> getKeySet() {
        if (keySet != null) {
            return keySet;
        }
        if (CollectionUtils.isEmpty(properties)) {
            return Collections.EMPTY_SET;
        }
        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        for (Object each : properties.keySet()) {
            builder.add(each.toString());
        }
        keySet = builder.build();
        return keySet;
    }

    private StringManager(String fileName) {
        properties = new Properties();
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            properties.load(new InputStreamReader(resource.getInputStream(), "utf-8"));
        } catch (IOException e) {
            log.error("文件：" + fileName + " 读取异常 ! ", e);
        }
    }

    /**
     * 以相对路径读取properties中的值
     *
     * @param fileName 文件名
     * @return
     */
    public static StringManager getStringManageByFileName(String fileName) {
        StringManager mgr = cache.get(fileName);
        if (mgr == null) {
            synchronized (cache) {
                mgr = cache.computeIfAbsent(fileName, StringManager::new);
            }
        }
        return mgr;
    }


    public String getValue(String key) {
        return properties.getProperty(key);
    }

    public String getValue(String key, Object... args) {
        String value = getValue(key);
        return formatKeyString(value, args);
    }

    public int getIntValue(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NullPointerException();
        }
        return Integer.parseInt(value);
    }


    public static String formatKeyString(String key, Object... args) {
        try {
            return MessageFormat.format(key, args);
        } catch (IllegalArgumentException iae) {
            StringBuilder buf = new StringBuilder();
            buf.append(key);
            for (int i = 0; i < args.length; i++) {
                buf.append(" arg[").append(i).append("]=").append(args[i]);
            }
            return buf.toString();
        }
    }
}

package org.zclpro.common.msg;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class ResultMsgUtil {

    public static Map<String, Object> success(Map<String, Object> data) {
        data.put("resultCode", 0);
        data.put("resultMsg", "success");
        return data;
    }

    public final static Map<String, Object> SUCCESS = ImmutableMap.of("resultCode", 0, "resultMsg", "success");


    public static Map<String, Object> success(String key, Object value) {
        return ImmutableMap.<String, Object>builder().putAll(SUCCESS).put(key, value).build();
    }

    private static Map<String, Object> result(int code, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("resultCode", code);
        map.put("resultMsg", message);
        return map;
    }

    public static Map<String, Object> fail(ResultErrorCode resultErrorCode) {
        return result(resultErrorCode.getResultCode(), resultErrorCode.getResultMsg());
    }

    public static Map<String, Object> fail(String message) {
        return result(-1, message);
    }
}

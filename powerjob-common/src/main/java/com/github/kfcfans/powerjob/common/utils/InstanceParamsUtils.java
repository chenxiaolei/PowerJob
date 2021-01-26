package com.github.kfcfans.powerjob.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenlei
 * @version 1.0
 * @since 2021/1/26 21:03
 */
public class InstanceParamsUtils {

    public static String getDefault(long firstTriggerTime) {
        Map<String, String> defaultInstanceParams = new HashMap<>();
        defaultInstanceParams.put("firstTriggerTime", String.valueOf(firstTriggerTime));
        return JsonUtils.toJSONString(defaultInstanceParams);
    }
}

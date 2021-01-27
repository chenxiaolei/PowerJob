package com.github.kfcfans.powerjob.server.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import com.github.kfcfans.powerjob.common.utils.JsonUtils;
import com.github.kfcfans.powerjob.server.persistence.core.model.IEvaluatedExpressionJob;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenlei
 * @version 1.0
 * @since 2021/1/26 21:03
 */
public class ParamsUtils {

    private final static TemplateEngine templateEngine;

    static {
        templateEngine = TemplateUtil.createEngine(new TemplateConfig().setCustomEngine(FreemarkerEngine.class));
    }

    public static String eval(String templateStr, Map<?, ?> bindingMap) {
        Template template = templateEngine.getTemplate(templateStr);
        return template.render(bindingMap);
    }

    public static String eval(IEvaluatedExpressionJob jobInfo) {
        return evalIfNull(null, jobInfo);
    }

    public static String evalIfNull(String params, IEvaluatedExpressionJob jobInfo) {
        if (StringUtils.isNotBlank(params)) {
            return params;
        }
        long triggerTime = jobInfo.getNextTriggerTime() != null ? jobInfo.getNextTriggerTime() : System.currentTimeMillis();

        if (StringUtils.isBlank(jobInfo.getEvaluatedExpression())) {
            //如果没有 给个实例首次触发时间
            Map<String, Object> defaultInstanceParams = new HashMap<>();
            defaultInstanceParams.put("instanceFirstTriggerTime", triggerTime);
            return JsonUtils.toJSONString(defaultInstanceParams);
        }
        return eval(jobInfo.getEvaluatedExpression(), Dict.create()
                .set("jobInfo", jobInfo)
                .set("instanceFirstTriggerTime", triggerTime));
    }


    public static void main(String[] args) {
        String result = eval(
                "{ \"startTime\": ${(instanceFirstTriggerTime)?number_to_date?string(\"yyyy-MM-dd\")} }",
                Dict.create()
                        .set("instanceFirstTriggerTime", System.currentTimeMillis())
        );
        System.out.println(result);
    }
}

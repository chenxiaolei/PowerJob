package com.github.kfcfans.powerjob.server.service;

import cn.hutool.core.lang.Dict;
import com.github.kfcfans.powerjob.common.ExecuteType;
import com.github.kfcfans.powerjob.common.OmsConstant;
import com.github.kfcfans.powerjob.common.ProcessorType;
import com.github.kfcfans.powerjob.common.TimeExpressionType;
import com.github.kfcfans.powerjob.server.common.utils.CronExpression;
import com.github.kfcfans.powerjob.server.persistence.core.model.IEvaluatedExpressionJob;
import com.github.kfcfans.powerjob.server.persistence.core.model.JobInfoDO;
import com.github.kfcfans.powerjob.server.persistence.core.model.WorkflowInfoDO;
import com.github.kfcfans.powerjob.server.utils.ParamsUtils;
import com.github.kfcfans.powerjob.server.web.request.ParamsExpressionRequest;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 校验服务
 *
 * @author tjq
 * @since 2020/11/28
 */
public class ValidateService {

    private static final int NEXT_N_TIMES = 10;


    public static String calculateParamsExpression(ParamsExpressionRequest request) throws Exception {
        if(StringUtils.isBlank(request.getParamsExpression())){
            return "";
        }
        long now = System.currentTimeMillis();
        IEvaluatedExpressionJob jobInfo;
        if (request.getIsWorkflowInfoInitParams()) {
            jobInfo = WorkflowInfoDO.builder()
                    .id(1234567890L).wfName("wfName").wfDescription("wfDescription").appId(9876543210L).peDAG("peDAG")
                    .defaultInitParamsExpression(request.getParamsExpression()).timeExpressionType(TimeExpressionType.CRON.getV()).timeExpression("* * * * * ?").maxWfInstanceNum(1).status(1)
                    .nextTriggerTime(now).notifyUserIds("1,2,3").gmtCreate(new Date()).gmtModified(new Date())
                    .build();
        } else {
            jobInfo = JobInfoDO.builder()
                    .id(1234567890L).jobName("jobName").jobDescription("jobDescription").appId(9876543210L).jobParams("{ \"jobParam1\": \"test\" }")
                    .defaultInstanceParamsExpression(request.getParamsExpression()).timeExpressionType(TimeExpressionType.CRON.getV()).timeExpression("* * * * * ?").executeType(ExecuteType.STANDALONE.getV()).processorType(ProcessorType.EMBEDDED_JAVA.getV())
                    .processorInfo("processorInfo").maxInstanceNum(1).concurrency(1).instanceTimeLimit(500000L).instanceRetryNum(3)
                    .taskRetryNum(3).status(1).nextTriggerTime(now).minCpuCores(0).minMemorySpace(0)
                    .minDiskSpace(0).designatedWorkers("192.168.0.1:7700,192.168.0.2:7700").maxWorkerCount(3).notifyUserIds("1,2,3")
                    .gmtCreate(new Date()).gmtModified(new Date())
                    .build();
        }
        Dict bindingMap = Dict.create()
                .set("jobInfo", jobInfo)
                .set("instanceFirstTriggerTime", now);

        return ParamsUtils.eval(request.getParamsExpression(), bindingMap);
    }

    /**
     * 计算指定时间表达式接下来的运行状况
     *
     * @param timeExpressionType 时间表达式类型
     * @param timeExpression     时间表达式
     * @return 最近 N 次运行的时间
     * @throws Exception 异常
     */
    public static List<String> calculateNextTriggerTime(TimeExpressionType timeExpressionType, String timeExpression) throws Exception {
        switch (timeExpressionType) {
            case API:
                return Lists.newArrayList(OmsConstant.NONE);
            case WORKFLOW:
                return Lists.newArrayList("VALID: depends on workflow");
            case CRON:
                return calculateCronExpression(timeExpression);
            case FIXED_RATE:
                return calculateFixRate(timeExpression);
            case FIXED_DELAY:
                return Lists.newArrayList("VALID: depends on execution cost time");
        }
        // impossible
        return Collections.emptyList();
    }


    private static List<String> calculateFixRate(String timeExpression) {
        List<String> result = Lists.newArrayList();
        long delay = Long.parseLong(timeExpression);
        for (int i = 0; i < NEXT_N_TIMES; i++) {
            long nextTime = System.currentTimeMillis() + i * delay;
            result.add(DateFormatUtils.format(nextTime, OmsConstant.TIME_PATTERN));
        }
        return result;
    }

    private static List<String> calculateCronExpression(String expression) throws ParseException {
        CronExpression cronExpression = new CronExpression(expression);
        List<String> result = Lists.newArrayList();
        Date time = new Date();
        for (int i = 0; i < NEXT_N_TIMES; i++) {
            Date nextValidTime = cronExpression.getNextValidTimeAfter(time);
            if (nextValidTime == null) {
                break;
            }
            result.add(DateFormatUtils.format(nextValidTime.getTime(), OmsConstant.TIME_PATTERN));
            time = nextValidTime;
        }
        if (result.isEmpty()) {
            result.add("INVALID: no next validate schedule time");
        }
        return result;
    }
}

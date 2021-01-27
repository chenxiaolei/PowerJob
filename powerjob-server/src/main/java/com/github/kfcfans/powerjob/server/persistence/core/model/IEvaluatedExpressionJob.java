package com.github.kfcfans.powerjob.server.persistence.core.model;

/**
 * @author chenlei
 * @version 1.0
 * @since 2021/1/27 16:09
 */
public interface IEvaluatedExpressionJob {
    Long getNextTriggerTime();

    String getEvaluatedExpression();
}

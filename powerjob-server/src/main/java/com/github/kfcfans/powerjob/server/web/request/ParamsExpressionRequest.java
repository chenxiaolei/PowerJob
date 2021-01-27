package com.github.kfcfans.powerjob.server.web.request;

import lombok.Data;

/**
 * @author chenlei
 * @version 1.0
 * @since 2021/1/27 19:40
 */
@Data
public class ParamsExpressionRequest {

    private Boolean isWorkflowInfoInitParams;
    private String paramsExpression;
}

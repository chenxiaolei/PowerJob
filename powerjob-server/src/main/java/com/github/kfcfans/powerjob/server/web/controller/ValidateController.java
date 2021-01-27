package com.github.kfcfans.powerjob.server.web.controller;

import com.github.kfcfans.powerjob.common.TimeExpressionType;
import com.github.kfcfans.powerjob.common.response.ResultDTO;
import com.github.kfcfans.powerjob.server.service.ValidateService;
import com.github.kfcfans.powerjob.server.web.request.ParamsExpressionRequest;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 校验控制器
 *
 * @author tjq
 * @since 2020/11/28
 */
@RestController
@RequestMapping("/validate")
public class ValidateController {

    @GetMapping("/timeExpression")
    public ResultDTO<List<String>> checkTimeExpression(TimeExpressionType timeExpressionType, String timeExpression) {
        try {
            return ResultDTO.success(ValidateService.calculateNextTriggerTime(timeExpressionType, timeExpression));
        } catch (Exception e) {
            return ResultDTO.success(Lists.newArrayList(ExceptionUtils.getMessage(e)));
        }
    }


    @PostMapping("/paramsExpression")
    public ResultDTO<String> checkParamsExpression(@RequestBody ParamsExpressionRequest paramsExpressionRequest) {
        try {
            return ResultDTO.success(ValidateService.calculateParamsExpression(paramsExpressionRequest));
        } catch (Exception e) {
            return ResultDTO.success(ExceptionUtils.getMessage(e));
        }
    }
}

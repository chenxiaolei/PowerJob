package com.github.kfcfans.powerjob.common.request.query;

import com.github.kfcfans.powerjob.common.PowerQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * Query InstanceInfo
 *
 */
@Getter
@Setter
@Accessors(chain = true)
public class InstanceInfoQuery extends PowerQuery {

    private Long instanceIdEq;
    private Long instanceIdLt;
    private Long instanceIdGt;

    private Long runningTimesEq;
    private Long runningTimesLt;
    private Long runningTimesGt;

    private Long jobIdEq;
    private Long wfInstanceIdEq;

    private List<Integer> statusIn;
    private Integer typeEq;

    private Long expectedTriggerTimeGt;
    private Long expectedTriggerTimeLt;

    private Long actualTriggerTimeGt;
    private Long actualTriggerTimeLt;

    private Long firstTriggerTimeGt;
    private Long firstTriggerTimeLt;

    private Long finishedTimeGt;
    private Long finishedTimeLt;

    private Long lastReportTimeGt;
    private Long lastReportTimeLt;

    private Date gmtCreateLt;
    private Date gmtCreateGt;

    private Date gmtModifiedLt;
    private Date gmtModifiedGt;
}

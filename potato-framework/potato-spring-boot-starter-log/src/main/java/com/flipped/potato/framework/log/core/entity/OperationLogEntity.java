package com.flipped.potato.framework.log.core.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志实体
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2023-09-22 11:07:17
 */
@Data
public class OperationLogEntity {

    private Long logId;


    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求类
     */
    private String className;

    /**
     * 请求函数
     */
    private String methodName;

    /**
     * 操作类型
     */
    private Integer methodType;

    /**
     * 操作描述
     */
    private String methodDescription;

    /**
     * 服务器IP地址
     */
    private String serverIp;

    /**
     * 客户端IP地址
     */
    private String clientIp;

    /**
     * 返回结果，是否成功
     */
    private Integer success;

    /**
     * 返回信息
     */
    private String responseMessage;

    /**
     * 消耗时间
     */
    private Long spendTime;

    /**
     * 操作用户id
     */
    private Long operationUserid;

    /**
     * 操作用户
     */
    private String operationUsername;

    /**
     * 创建时间
     */
    private LocalDateTime operationTime;

}

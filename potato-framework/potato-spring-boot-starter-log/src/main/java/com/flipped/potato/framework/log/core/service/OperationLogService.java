package com.flipped.potato.framework.log.core.service;

import com.flipped.potato.framework.log.core.entity.OperationLogEntity;

/**
 * 操作日志逻辑接口
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2023-09-22 11:04:16
 */
public interface OperationLogService {

    /**
     * 保存操作日志
     *
     * @param operationLogEntity 操作日志信息
     */
    void save(OperationLogEntity operationLogEntity);
}

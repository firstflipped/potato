package com.flipped.potato.framework.log.core.service.impl;

import com.flipped.potato.framework.log.core.entity.OperationLogEntity;
import com.flipped.potato.framework.log.core.service.OperationLogService;
import org.springframework.scheduling.annotation.Async;

/**
 * 操作日志逻辑实现
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2023-09-22 11:04:55
 */
public class OperationLogServiceImpl implements OperationLogService {


    @Async
    @Override
    public void save(OperationLogEntity operationLogEntity) {

    }
}

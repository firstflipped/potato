package com.flipped.potato.framework.log.config;

import com.flipped.potato.framework.log.core.aspect.OperationLogAspect;
import com.flipped.potato.framework.log.core.service.OperationLogService;
import com.flipped.potato.framework.log.core.service.impl.OperationLogServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 操作日志自动配置类
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2023-09-13 17:14:12
 */
@AutoConfiguration
public class PotatoOperateLogAutoConfiguration {

    /**
     * 将日志切面注入到spring容器
     *
     * @return 日志切面
     */
    @Bean
    public OperationLogAspect operationLogAspect() {
        return new OperationLogAspect();
    }


    @Bean
    @ConditionalOnMissingBean
    public OperationLogService operationLogService() {
        return new OperationLogServiceImpl();
    }

}

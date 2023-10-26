package com.flipped.potato.framework.generate.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 代码生成自动配置
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2023-09-11 14:43:47
 */
@AutoConfiguration
@EnableConfigurationProperties(PotatoGenerateProperties.class)
public class PotatoGenerateAutoConfiguration {
}

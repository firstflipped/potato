package com.flipped.potato.framework.generate.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * 代码自动生成配置类
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2023-09-11 14:43:47
 */
@Data
@Validated
@ConfigurationProperties("potato.generate")
public class PotatoGenerateProperties {

    /**
     * 是否开启，默认为 true 关闭
     */
    private Boolean enabled = true;

    /**
     * 数据库配置
     */
    @NotNull(message = "数据库配置不能为空")
    private DataBaseProperties database;

    /**
     * 代码配置
     */
    @NotNull(message = "代码配置不能为空")
    private CodeProperties code;

    /**
     * 数据库配置类
     */
    @Data
    @Valid
    public static class DataBaseProperties {

        /**
         * 调度器地址
         */
        @NotEmpty(message = "数据库类型不能为空")
        private String type;

    }

    /**
     * 代码配置类
     */
    @Data
    @Valid
    public static class CodeProperties {

        /**
         * 自动删除表前缀
         * 默认为False
         */
        private static final Boolean AUTO_REMOVE_TABLE_PREFIX = false;

        private static final String TABLE_PREFIX = "";

        /**
         * 作者
         */
        @NotEmpty(message = "作者不能为空")
        private String author;

        /**
         * 包名
         */
        @NotEmpty(message = "包名不能为空")
        private String packageName;

        /**
         * 自动去除表前缀
         */
        private Boolean autoRemoveTablePrefix = AUTO_REMOVE_TABLE_PREFIX;

        /**
         * 表前缀
         */
        private String tablePrefix = TABLE_PREFIX;

    }

}

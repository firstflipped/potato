package com.flipped.potato.framework.log.core.entity.emnus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作日志的操作类型
 *
 * @author ruoyi
 */
@Getter
@AllArgsConstructor
public enum OperationTypeEnum {

    /**
     * 查询
     * 绝大多数情况下，不会记录查询动作，因为过于大量显得没有意义。
     * 在有需要的时候，通过声明 {@link com.flipped.potato.framework.log.core.annotation.OperationLog} 注解来记录
     */
    GET(1, "查询"),

    /**
     * 新增
     */
    CREATE(2, "新增"),

    /**
     * 修改
     */
    UPDATE(3, "修改"),

    /**
     * 删除
     */
    DELETE(4, "删除"),

    /**
     * 导出
     */
    EXPORT(5, "导出"),

    /**
     * 导入
     */
    IMPORT(6, "导入"),

    /**
     * 其它
     * 在无法归类时，可以选择使用其它。因为还有操作名可以进一步标识
     */
    OTHER(0, "其它");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String description;

}

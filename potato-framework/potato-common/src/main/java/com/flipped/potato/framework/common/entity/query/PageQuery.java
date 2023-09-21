package com.flipped.potato.framework.common.entity.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 自定义统一分页入参
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2023-09-21 11:20:53
 */
@Data
public class PageQuery {

    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;

    /**
     * 当前页码
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    private Integer pn = PAGE_NUMBER;

    /**
     * 每页条数
     */
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    @Max(value = 100, message = "页码最大值为 100")
    private Integer ps = PAGE_SIZE;

}

package com.flipped.potato.framework.common.entity.query;

import lombok.Data;

/**
 * 自定义统一排序如参
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2023-09-21 13:55:29
 */
@Data
public class SortedQuery {

    /**
     * 升序
     */
    private static final String ASC = "ASC";

    /**
     * 降序
     */
    private static final String DESC = "DESC";


    /**
     * 排序字段
     */
    private String field;

    /**
     * 顺序 默认升序
     */
    private String order = ASC;

}

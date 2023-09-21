package com.flipped.potato.framework.common.entity.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 自定义统一分页封装实体
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2022-04-11 19:35:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PotatoPage<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 当前页数
     */
    private Long pageNumber;

    /**
     * 每页记录数
     */
    private Long pageSize;

    /**
     * 列表数据
     */
    private List<T> list;


    /**
     * 组装自定义统一分页返回信息
     *
     * @param total      总条数
     * @param pages      总页数
     * @param pageNumber 页码
     * @param pageSize   每页条数
     * @param list       数据
     * @return 自定义统一分页返回信息
     */
    public static <T> PotatoPage<T> restPage(Long total, Long pages, Long pageNumber, Long pageSize, List<T> list) {
        return PotatoPage.<T>builder()
                .total(total)
                .pages(pages)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .list(list)
                .build();
    }

//    public static <T> PotatoPage<T> restPage(IPage<T> iPage) {
//        return PotatoPage.<T>builder()
//                .total(iPage.getTotal())
//                .pages(iPage.getPages())
//                .pageNumber(iPage.getCurrent())
//                .pageSize(iPage.getSize())
//                .list(iPage.getRecords())
//                .build();
//    }


}

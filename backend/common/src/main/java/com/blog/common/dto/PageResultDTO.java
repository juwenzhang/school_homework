package com.blog.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果DTO
 */
@Data
public class PageResultDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPages;
    private List<T> list;

    /**
     * 构建分页结果
     */
    public static <T> PageResultDTO<T> build(Long total, Integer pageNum, Integer pageSize, List<T> list) {
        PageResultDTO<T> result = new PageResultDTO<>();
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotalPages((int) Math.ceil((double) total / pageSize));
        result.setList(list);
        return result;
    }
}
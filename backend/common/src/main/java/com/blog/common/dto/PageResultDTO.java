package com.blog.common.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果DTO
 */
public class PageResultDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPages;
    private List<T> list;

    // Getter and Setter methods
    public Long getTotal() {
        return total;
    }
    
    public void setTotal(Long total) {
        this.total = total;
    }
    
    public Integer getPageNum() {
        return pageNum;
    }
    
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    public Integer getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
    
    public List<T> getList() {
        return list;
    }
    
    public void setList(List<T> list) {
        this.list = list;
    }

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
package com.blog.common.dto;

import com.blog.common.constant.SystemConstants;

/**
 * 分页查询参数DTO
 */
public class PageQueryDTO {
    private Integer pageNum = SystemConstants.DEFAULT_PAGE_NUM;
    private Integer pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
    private String keyword;
    private String sortBy;
    private String order = "desc";

    /**
     * 检查并修正分页参数
     */
    public void validateAndFix() {
        if (pageNum < 1) {
            pageNum = SystemConstants.DEFAULT_PAGE_NUM;
        }
        if (pageSize < 1) {
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        if (pageSize > SystemConstants.MAX_PAGE_SIZE) {
            pageSize = SystemConstants.MAX_PAGE_SIZE;
        }
    }

    /**
     * 获取偏移量
     */
    public int getOffset() {
        return (pageNum - 1) * pageSize;
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
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public String getSortBy() {
        return sortBy;
    }
    
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
    
    public String getOrder() {
        return order;
    }
    
    public void setOrder(String order) {
        this.order = order;
    }
}
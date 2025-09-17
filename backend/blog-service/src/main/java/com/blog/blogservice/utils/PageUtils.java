package com.blog.blogservice.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页工具类
 */
public class PageUtils {

    /**
     * 创建分页请求对象，默认升序
     */
    public static Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size);
    }

    /**
     * 创建分页请求对象，支持排序
     */
    public static Pageable createPageable(int page, int size, String... sortFields) {
        if (sortFields == null || sortFields.length == 0) {
            return PageRequest.of(page, size);
        }
        Sort sort = Sort.by(Sort.Direction.ASC, sortFields);
        return PageRequest.of(page, size, sort);
    }

    /**
     * 创建分页请求对象，支持自定义排序方向
     */
    public static Pageable createPageable(int page, int size, Sort.Direction direction, String... sortFields) {
        if (sortFields == null || sortFields.length == 0) {
            return PageRequest.of(page, size);
        }
        Sort sort = Sort.by(direction, sortFields);
        return PageRequest.of(page, size, sort);
    }

    /**
     * 创建分页请求对象，支持多字段排序
     */
    public static Pageable createPageable(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }

    /**
     * 检查页码是否有效
     */
    public static int checkPage(int page) {
        return Math.max(0, page);
    }

    /**
     * 检查每页大小是否有效
     */
    public static int checkSize(int size, int maxSize) {
        size = Math.max(1, size);
        return Math.min(size, maxSize);
    }

    /**
     * 检查每页大小是否有效，默认最大50
     */
    public static int checkSize(int size) {
        return checkSize(size, 50);
    }

    /**
     * 计算总页数
     */
    public static int calculateTotalPages(int totalElements, int pageSize) {
        if (pageSize <= 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalElements / pageSize);
    }

    /**
     * 计算当前页的起始元素索引
     */
    public static int calculateStartIndex(int page, int pageSize) {
        page = checkPage(page);
        pageSize = checkSize(pageSize);
        return page * pageSize;
    }
}
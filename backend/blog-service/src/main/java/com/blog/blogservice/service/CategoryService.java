package com.blog.blogservice.service;

import com.blog.blogservice.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {

    /**
     * 创建分类
     * @param category 分类信息
     * @return 创建的分类
     */
    Category createCategory(Category category);

    /**
     * 更新分类
     * @param id 分类ID
     * @param category 分类信息
     * @return 更新后的分类
     */
    Category updateCategory(Long id, Category category);

    /**
     * 删除分类
     * @param id 分类ID
     */
    void deleteCategory(Long id);

    /**
     * 获取分类详情
     * @param id 分类ID
     * @return 分类详情
     */
    Category getCategoryById(Long id);

    /**
     * 获取所有分类
     * @return 分类列表
     */
    List<Category> getAllCategories();

    /**
     * 根据父分类ID获取子分类
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> getCategoriesByParentId(Long parentId);

    /**
     * 根据分类名称获取分类
     * @param name 分类名称
     * @return 分类信息
     */
    Category getCategoryByName(String name);

    /**
     * 启用分类
     * @param id 分类ID
     */
    Category enableCategory(Long id);

    /**
     * 禁用分类
     * @param id 分类ID
     */
    Category disableCategory(Long id);

    /**
     * 检查分类名称是否存在
     * @param name 分类名称
     * @param id 分类ID（可选，用于排除当前分类）
     * @return 是否存在
     */
    boolean existsCategoryName(String name, Long id);
}
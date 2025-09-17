package com.blog.blogservice.repository;

import com.blog.blogservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 分类仓库接口
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 根据分类名称查询分类
     * @param name 分类名称
     * @return 分类信息
     */
    Optional<Category> findByName(String name);

    /**
     * 查询父分类下的子分类
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> findByParentId(Long parentId);

    /**
     * 根据状态查询分类
     * @param status 分类状态
     * @return 分类列表
     */
    List<Category> findByStatus(Integer status);

    /**
     * 检查分类名称是否存在
     * @param name 分类名称
     * @return 是否存在
     */
    boolean existsByName(String name);
}
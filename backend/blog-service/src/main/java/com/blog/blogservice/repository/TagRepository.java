package com.blog.blogservice.repository;

import com.blog.blogservice.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 标签仓库接口
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * 根据标签名称查询标签
     * @param name 标签名称
     * @return 标签信息
     */
    Optional<Tag> findByName(String name);

    /**
     * 根据名称列表查询标签
     * @param names 标签名称列表
     * @return 标签列表
     */
    List<Tag> findByNameIn(List<String> names);

    /**
     * 检查标签名称是否存在
     * @param name 标签名称
     * @return 是否存在
     */
    boolean existsByName(String name);
}
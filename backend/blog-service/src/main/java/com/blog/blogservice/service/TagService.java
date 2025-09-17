package com.blog.blogservice.service;

import com.blog.blogservice.entity.Tag;

import java.util.List;

/**
 * 标签服务接口
 */
public interface TagService {

    /**
     * 创建标签
     * @param tag 标签信息
     * @return 创建的标签
     */
    Tag createTag(Tag tag);

    /**
     * 更新标签
     * @param id 标签ID
     * @param tag 标签信息
     * @return 更新后的标签
     */
    Tag updateTag(Long id, Tag tag);

    /**
     * 删除标签
     * @param id 标签ID
     */
    void deleteTag(Long id);

    /**
     * 获取标签详情
     * @param id 标签ID
     * @return 标签详情
     */
    Tag getTagById(Long id);

    /**
     * 获取所有标签
     * @return 标签列表
     */
    List<Tag> getAllTags();

    /**
     * 根据标签名称获取标签
     * @param name 标签名称
     * @return 标签信息
     */
    Tag getTagByName(String name);

    /**
     * 根据标签名称列表获取标签
     * @param names 标签名称列表
     * @return 标签列表
     */
    List<Tag> getTagsByNames(List<String> names);

    /**
     * 检查标签名称是否存在
     * @param name 标签名称
     * @param id 标签ID（可选，用于排除当前标签）
     * @return 是否存在
     */
    boolean existsTagName(String name, Long id);
}
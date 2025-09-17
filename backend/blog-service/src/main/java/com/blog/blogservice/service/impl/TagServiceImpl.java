package com.blog.blogservice.service.impl;

import com.blog.blogservice.entity.Tag;
import com.blog.blogservice.repository.TagRepository;
import com.blog.blogservice.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 标签服务实现类
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    @Transactional
    public Tag createTag(Tag tag) {
        // 检查标签名称是否已存在
        if (tagRepository.existsByName(tag.getName())) {
            throw new RuntimeException("Tag name already exists");
        }
        tag.setCreatedTime(LocalDateTime.now());
        tag.setUpdatedTime(LocalDateTime.now());
        tag.setArticleCount(0);
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public Tag updateTag(Long id, Tag tag) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isPresent()) {
            Tag existingTag = optionalTag.get();
            
            // 检查标签名称是否已存在（排除当前标签）
            if (existsTagName(tag.getName(), id)) {
                throw new RuntimeException("Tag name already exists");
            }
            
            existingTag.setName(tag.getName());
            existingTag.setDescription(tag.getDescription());
            existingTag.setColor(tag.getColor());
            existingTag.setSort(tag.getSort());
            existingTag.setUpdatedTime(LocalDateTime.now());
            return tagRepository.save(existingTag);
        }
        throw new RuntimeException("Tag not found");
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        if (tagRepository.existsById(id)) {
            // 检查标签是否有文章使用
            Tag tag = tagRepository.findById(id).orElseThrow();
            if (tag.getArticleCount() > 0) {
                throw new RuntimeException("Tag is used by articles, cannot be deleted");
            }
            tagRepository.deleteById(id);
        } else {
            throw new RuntimeException("Tag not found");
        }
    }

    @Override
    public Tag getTagById(Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        return optionalTag.orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getTagByName(String name) {
        Optional<Tag> optionalTag = tagRepository.findByName(name);
        return optionalTag.orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    @Override
    public List<Tag> getTagsByNames(List<String> names) {
        return tagRepository.findByNameIn(names);
    }

    @Override
    public boolean existsTagName(String name, Long id) {
        Optional<Tag> optionalTag = tagRepository.findByName(name);
        if (optionalTag.isPresent()) {
            // 如果存在同名标签，检查是否是当前标签
            return !optionalTag.get().getId().equals(id);
        }
        return false;
    }
}
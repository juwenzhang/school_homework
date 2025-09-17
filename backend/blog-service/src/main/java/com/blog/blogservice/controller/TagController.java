package com.blog.blogservice.controller;

import com.blog.blogservice.entity.Tag;
import com.blog.blogservice.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签控制器
 */
@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 创建标签
     */
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag createdTag = tagService.createTag(tag);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    /**
     * 更新标签
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        Tag updatedTag = tagService.updateTag(id, tag);
        return ResponseEntity.ok(updatedTag);
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取标签详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        Tag tag = tagService.getTagById(id);
        return ResponseEntity.ok(tag);
    }

    /**
     * 获取所有标签
     */
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    /**
     * 根据标签名称获取标签
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Tag> getTagByName(@PathVariable String name) {
        Tag tag = tagService.getTagByName(name);
        return ResponseEntity.ok(tag);
    }

    /**
     * 根据标签名称列表获取标签
     */
    @PostMapping("/names")
    public ResponseEntity<List<Tag>> getTagsByNames(@RequestBody List<String> names) {
        List<Tag> tags = tagService.getTagsByNames(names);
        return ResponseEntity.ok(tags);
    }

    /**
     * 检查标签名称是否存在
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsTagName(@RequestParam String name,
                                                @RequestParam(required = false) Long id) {
        boolean exists = tagService.existsTagName(name, id);
        return ResponseEntity.ok(exists);
    }
}
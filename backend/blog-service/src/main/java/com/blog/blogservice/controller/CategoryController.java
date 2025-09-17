package com.blog.blogservice.controller;

import com.blog.blogservice.entity.Category;
import com.blog.blogservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 创建分类
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取分类详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * 获取所有分类
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * 获取父分类下的子分类
     */
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Category>> getCategoriesByParentId(@PathVariable Long parentId) {
        List<Category> categories = categoryService.getCategoriesByParentId(parentId);
        return ResponseEntity.ok(categories);
    }

    /**
     * 根据分类名称获取分类
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
        Category category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(category);
    }

    /**
     * 启用分类
     */
    @PostMapping("/{id}/enable")
    public ResponseEntity<Category> enableCategory(@PathVariable Long id) {
        Category category = categoryService.enableCategory(id);
        return ResponseEntity.ok(category);
    }

    /**
     * 禁用分类
     */
    @PostMapping("/{id}/disable")
    public ResponseEntity<Category> disableCategory(@PathVariable Long id) {
        Category category = categoryService.disableCategory(id);
        return ResponseEntity.ok(category);
    }

    /**
     * 检查分类名称是否存在
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsCategoryName(@RequestParam String name,
                                                     @RequestParam(required = false) Long id) {
        boolean exists = categoryService.existsCategoryName(name, id);
        return ResponseEntity.ok(exists);
    }
}
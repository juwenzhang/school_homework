package com.blog.blogservice.service.impl;

import com.blog.blogservice.entity.Category;
import com.blog.blogservice.repository.CategoryRepository;
import com.blog.blogservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 分类服务实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category createCategory(Category category) {
        // 检查分类名称是否已存在
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category name already exists");
        }
        category.setCreatedTime(LocalDateTime.now());
        category.setUpdatedTime(LocalDateTime.now());
        category.setArticleCount(0);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, Category category) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();
            
            // 检查分类名称是否已存在（排除当前分类）
            if (existsCategoryName(category.getName(), id)) {
                throw new RuntimeException("Category name already exists");
            }
            
            existingCategory.setName(category.getName());
            existingCategory.setDescription(category.getDescription());
            existingCategory.setParentId(category.getParentId());
            existingCategory.setIcon(category.getIcon());
            existingCategory.setSort(category.getSort());
            existingCategory.setUpdatedTime(LocalDateTime.now());
            return categoryRepository.save(existingCategory);
        }
        throw new RuntimeException("Category not found");
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            // 检查是否有子分类
            List<Category> subCategories = categoryRepository.findByParentId(id);
            if (!subCategories.isEmpty()) {
                throw new RuntimeException("Category has subcategories, cannot be deleted");
            }
            categoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getCategoriesByParentId(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    @Override
    public Category getCategoryByName(String name) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);
        return optionalCategory.orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    @Transactional
    public Category enableCategory(Long id) {
        Category category = getCategoryById(id);
        category.setStatus(1);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category disableCategory(Long id) {
        Category category = getCategoryById(id);
        category.setStatus(0);
        return categoryRepository.save(category);
    }

    @Override
    public boolean existsCategoryName(String name, Long id) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);
        if (optionalCategory.isPresent()) {
            // 如果存在同名分类，检查是否是当前分类
            return !optionalCategory.get().getId().equals(id);
        }
        return false;
    }
}
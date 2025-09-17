import { create } from 'zustand';
import type { Category, Tag } from '../types';
import { categoryAPI, tagAPI } from '../services/api';
import { mockCategories, mockTags, mockDelay } from '../services/mockData';
import TrackService from '../utils/track';

interface CategoryTagState {
  // 状态
  categories: Category[];
  tags: Tag[];
  loading: boolean;
  error: string | null;
  
  // 操作
  fetchCategories: () => Promise<void>;
  fetchTags: () => Promise<void>;
  fetchAll: () => Promise<void>;
}

// 判断是否使用模拟数据
const useMockData = process.env.NODE_ENV === 'development';

// 创建分类和标签状态管理
export const useCategoryTagStore = create<CategoryTagState>((set) => ({
  // 初始化状态
  categories: [],
  tags: [],
  loading: false,
  error: null,
  
  // 获取分类列表
  fetchCategories: async () => {
    set({ loading: true, error: null });
    
    try {
      let categories;
      
      if (useMockData) {
        // 使用模拟数据
        await mockDelay(); // 模拟网络延迟
        categories = mockCategories;
      } else {
        // 使用真实API
        categories = await categoryAPI.getCategories();
      }
      
      set({
        categories,
        loading: false
      });
      
      // 埋点：分类列表加载完成
      TrackService.track('categories_loaded', {
        count: categories.length
      });
      
    } catch (error) {
      console.error('Failed to fetch categories:', error);
      set({
        error: '获取分类列表失败，请稍后重试',
        loading: false
      });
      
      // 埋点：分类列表加载失败
      TrackService.track('categories_load_failed', {
        error: error instanceof Error ? error.message : 'Unknown error'
      });
    }
  },
  
  // 获取标签列表
  fetchTags: async () => {
    set({ loading: true, error: null });
    
    try {
      let tags;
      
      if (useMockData) {
        // 使用模拟数据
        await mockDelay(); // 模拟网络延迟
        tags = mockTags;
      } else {
        // 使用真实API
        tags = await tagAPI.getTags();
      }
      
      set({
        tags,
        loading: false
      });
      
      // 埋点：标签列表加载完成
      TrackService.track('tags_loaded', {
        count: tags.length
      });
      
    } catch (error) {
      console.error('Failed to fetch tags:', error);
      set({
        error: '获取标签列表失败，请稍后重试',
        loading: false
      });
      
      // 埋点：标签列表加载失败
      TrackService.track('tags_load_failed', {
        error: error instanceof Error ? error.message : 'Unknown error'
      });
    }
  },
  
  // 同时获取分类和标签
  fetchAll: async () => {
    try {
      // 创建临时引用以避免闭包问题
      const store = useCategoryTagStore.getState();
      await Promise.all([
        store.fetchCategories(),
        store.fetchTags()
      ]);
    } catch (error) {
      console.error('Failed to fetch categories and tags:', error);
    }
  }
}));
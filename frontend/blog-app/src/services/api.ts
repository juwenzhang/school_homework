import axios from 'axios';
import type { Article, Category, Tag, Comment, User } from '../types';
import TrackService from '../utils/track';

// 创建axios实例
const api = axios.create({
  baseURL: '/api', // 实际环境中应该使用配置的API base URL
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    // 可以在这里添加认证token等
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    console.error('API请求错误:', error);
    TrackService.track('api_error', {
      url: error.config?.url,
      method: error.config?.method,
      status: error.response?.status,
      message: error.message
    });
    return Promise.reject(error);
  }
);

/**
 * 文章相关API
 */
export const articleAPI = {
  /**
   * 获取文章列表
   * @param params 查询参数
   * @returns 文章列表和分页信息
   */
  getArticles: async (params: {
    page?: number;
    pageSize?: number;
    category?: string;
    tag?: string;
    search?: string;
  } = {}): Promise<{ list: Article[]; total: number }> => {
    const { page = 1, pageSize = 10, ...restParams } = params;
    TrackService.track('api_get_articles', params);
    return api.get('/articles', { params: { page, pageSize, ...restParams } });
  },

  /**
   * 获取文章详情
   * @param id 文章ID
   * @returns 文章详情
   */
  getArticleDetail: async (id: string): Promise<Article> => {
    TrackService.track('api_get_article_detail', { id });
    return api.get(`/articles/${id}`);
  },

  /**
   * 点赞文章
   * @param id 文章ID
   * @returns 点赞结果
   */
  likeArticle: async (id: string): Promise<{ success: boolean; likeCount: number }> => {
    TrackService.track('api_like_article', { id });
    return api.post(`/articles/${id}/like`);
  },

  /**
   * 获取文章评论
   * @param articleId 文章ID
   * @param page 页码
   * @param pageSize 每页数量
   * @returns 评论列表
   */
  getArticleComments: async (
    articleId: string,
    page: number = 1,
    pageSize: number = 20
  ): Promise<{ list: Comment[]; total: number }> => {
    TrackService.track('api_get_article_comments', { articleId, page, pageSize });
    return api.get(`/articles/${articleId}/comments`, { params: { page, pageSize } });
  },

  /**
   * 发表评论
   * @param articleId 文章ID
   * @param content 评论内容
   * @param parentId 父评论ID（可选）
   * @returns 评论结果
   */
  addComment: async (
    articleId: string,
    content: string,
    parentId?: string
  ): Promise<Comment> => {
    TrackService.track('api_add_comment', { articleId, parentId });
    return api.post(`/articles/${articleId}/comments`, { content, parentId });
  }
};

/**
 * 分类相关API
 */
export const categoryAPI = {
  /**
   * 获取分类列表
   * @returns 分类列表
   */
  getCategories: async (): Promise<Category[]> => {
    TrackService.track('api_get_categories');
    return api.get('/categories');
  },

  /**
   * 获取分类下的文章
   * @param categoryId 分类ID
   * @param page 页码
   * @param pageSize 每页数量
   * @returns 文章列表
   */
  getArticlesByCategory: async (
    categoryId: string,
    page: number = 1,
    pageSize: number = 10
  ): Promise<{ list: Article[]; total: number }> => {
    TrackService.track('api_get_articles_by_category', { categoryId, page, pageSize });
    return api.get(`/categories/${categoryId}/articles`, { params: { page, pageSize } });
  }
};

/**
 * 标签相关API
 */
export const tagAPI = {
  /**
   * 获取标签列表
   * @returns 标签列表
   */
  getTags: async (): Promise<Tag[]> => {
    TrackService.track('api_get_tags');
    return api.get('/tags');
  },

  /**
   * 获取标签下的文章
   * @param tagId 标签ID
   * @param page 页码
   * @param pageSize 每页数量
   * @returns 文章列表
   */
  getArticlesByTag: async (
    tagId: string,
    page: number = 1,
    pageSize: number = 10
  ): Promise<{ list: Article[]; total: number }> => {
    TrackService.track('api_get_articles_by_tag', { tagId, page, pageSize });
    return api.get(`/tags/${tagId}/articles`, { params: { page, pageSize } });
  }
};

/**
 * 用户相关API
 */
export const userAPI = {
  /**
   * 获取用户信息
   * @param id 用户ID
   * @returns 用户信息
   */
  getUserInfo: async (id: string): Promise<User> => {
    TrackService.track('api_get_user_info', { id });
    return api.get(`/users/${id}`);
  },

  /**
   * 登录
   * @param username 用户名
   * @param password 密码
   * @returns 登录结果
   */
  login: async (username: string, password: string): Promise<{
    token: string;
    user: User;
  }> => {
    TrackService.track('api_login', { username });
    const result = await api.post('/auth/login', { username, password });
    // 保存token到本地存储
    if (result.data.token) {
      localStorage.setItem('token', result.data.token);
    }
    return result.data;
  },

  /**
   * 登出
   */
  logout: async (): Promise<void> => {
    TrackService.track('api_logout');
    await api.post('/auth/logout');
    // 清除本地存储的token
    localStorage.removeItem('token');
  }
};

/**
 * 搜索相关API
 */
export const searchAPI = {
  /**
   * 搜索文章
   * @param query 搜索关键词
   * @param page 页码
   * @param pageSize 每页数量
   * @returns 搜索结果
   */
  searchArticles: async (
    query: string,
    page: number = 1,
    pageSize: number = 10
  ): Promise<{ list: Article[]; total: number }> => {
    TrackService.track('api_search_articles', { query, page, pageSize });
    return api.get('/search', { params: { query, page, pageSize } });
  }
};

export default { articleAPI, categoryAPI, tagAPI, userAPI, searchAPI };
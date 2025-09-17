import { create } from 'zustand';
import type { Article, Comment } from '../types';
import { articleAPI } from '../services/api';
import { getMockArticles, getMockArticleDetail, getMockArticleComments, addMockComment, mockDelay } from '../services/mockData';
import TrackService from '../utils/track';

interface ArticleState {
  // 状态
  articles: Article[];
  articleDetail: Article | null;
  totalArticles: number;
  comments: Comment[];
  totalComments: number;
  loading: boolean;
  error: string | null;
  commentLoading: boolean;
  commentError: string | null;
  currentPage: number;
  pageSize: number;
  selectedCategory: string | null;
  selectedTag: string | null;
  searchQuery: string;
  
  // 操作
  fetchArticles: (page?: number, pageSize?: number, category?: string, tag?: string, search?: string) => Promise<void>;
  fetchArticleDetail: (id: string) => Promise<void>;
  likeArticle: (id: string) => Promise<void>;
  fetchComments: (articleId: string, page?: number, pageSize?: number) => Promise<void>;
  addComment: (articleId: string, content: string, parentId?: string) => Promise<void>;
  setSelectedCategory: (category: string | null) => void;
  setSelectedTag: (tag: string | null) => void;
  setSearchQuery: (query: string) => void;
  resetFilters: () => void;
}

// 判断是否使用模拟数据
const useMockData = process.env.NODE_ENV === 'development';

// 创建文章状态管理
export const useArticleStore = create<ArticleState>((set, get) => ({
  // 初始化状态
  articles: [],
  articleDetail: null,
  totalArticles: 0,
  comments: [],
  totalComments: 0,
  loading: false,
  error: null,
  commentLoading: false,
  commentError: null,
  currentPage: 1,
  pageSize: 10,
  selectedCategory: null,
  selectedTag: null,
  searchQuery: '',
  
  // 获取文章列表
  fetchArticles: async (page, pageSize, category, tag, search) => {
    const currentPage = page || get().currentPage;
    const currentPageSize = pageSize || get().pageSize;
    const currentCategory = category || get().selectedCategory;
    const currentTag = tag || get().selectedTag;
    const currentSearch = search || get().searchQuery;
    
    set({ loading: true, error: null });
    
    try {
      let result;
      
      if (useMockData) {
        // 使用模拟数据
        await mockDelay(); // 模拟网络延迟
        // 将 null 转换为 undefined 以匹配函数参数类型
        const categoryParam = currentCategory === null ? undefined : currentCategory;
        const tagParam = currentTag === null ? undefined : currentTag;
        result = getMockArticles(currentPage, currentPageSize, categoryParam, tagParam, currentSearch);
      } else {
        // 使用真实API
        result = await articleAPI.getArticles({
          page: currentPage,
          pageSize: currentPageSize,
          category: currentCategory === null ? undefined : currentCategory,
          tag: currentTag === null ? undefined : currentTag,
          search: currentSearch
        });
      }
      
      set({
        articles: result.list,
        totalArticles: result.total,
        currentPage,
        loading: false
      });
      
      // 埋点：文章列表加载完成
      TrackService.track('articles_loaded', {
        page: currentPage,
        pageSize: currentPageSize,
        total: result.total,
        category: currentCategory,
        tag: currentTag,
        search: currentSearch
      });
      
    } catch (error) {
      console.error('Failed to fetch articles:', error);
      set({
        error: '获取文章列表失败，请稍后重试',
        loading: false
      });
      
      // 埋点：文章列表加载失败
      TrackService.track('articles_load_failed', {
        error: error instanceof Error ? error.message : 'Unknown error'
      });
    }
  },
  
  // 获取文章详情
  fetchArticleDetail: async (id) => {
    set({ loading: true, error: null });
    
    try {
      let articleDetail;
      
      if (useMockData) {
        // 使用模拟数据
        await mockDelay(); // 模拟网络延迟
        articleDetail = getMockArticleDetail(id);
      } else {
        // 使用真实API
        articleDetail = await articleAPI.getArticleDetail(id);
      }
      
      if (!articleDetail) {
        throw new Error('Article not found');
      }
      
      set({
        articleDetail,
        loading: false
      });
      
      // 埋点：文章详情加载完成
      TrackService.track('article_detail_loaded', {
        article_id: id,
        article_title: articleDetail.title
      });
      
    } catch (error) {
      console.error('Failed to fetch article detail:', error);
      set({
        error: '获取文章详情失败，请稍后重试',
        loading: false
      });
      
      // 埋点：文章详情加载失败
      TrackService.track('article_detail_load_failed', {
        article_id: id,
        error: error instanceof Error ? error.message : 'Unknown error'
      });
    }
  },
  
  // 点赞文章
  likeArticle: async (id) => {
    try {
      // 在实际环境中调用API
      if (!useMockData) {
        await articleAPI.likeArticle(id);
      }
      
      // 更新本地状态
      set(state => ({
        articles: state.articles.map(article => 
          article.id === id 
            ? { ...article, likeCount: article.likeCount + 1 }
            : article
        ),
        articleDetail: state.articleDetail?.id === id 
          ? { ...state.articleDetail, likeCount: state.articleDetail.likeCount + 1 }
          : state.articleDetail
      }));
      
      // 埋点：文章点赞成功
      TrackService.track('article_liked', { article_id: id });
      
    } catch (error) {
      console.error('Failed to like article:', error);
      
      // 埋点：文章点赞失败
      TrackService.track('article_like_failed', {
        article_id: id,
        error: error instanceof Error ? error.message : 'Unknown error'
      });
    }
  },
  
  // 设置选中的分类
  setSelectedCategory: (category) => {
    set({
      selectedCategory: category,
      selectedTag: null,
      currentPage: 1
    });
    
    // 重新获取文章列表
    get().fetchArticles();
    
    // 埋点：分类选择
    if (category) {
      TrackService.track('category_selected', { category });
    }
  },
  
  // 设置选中的标签
  setSelectedTag: (tag) => {
    set({
      selectedTag: tag,
      selectedCategory: null,
      currentPage: 1
    });
    
    // 重新获取文章列表
    get().fetchArticles();
    
    // 埋点：标签选择
    if (tag) {
      TrackService.track('tag_selected', { tag });
    }
  },
  
  // 设置搜索关键词
  setSearchQuery: (query) => {
    set({
      searchQuery: query,
      currentPage: 1
    });
    
    // 重新获取文章列表
    get().fetchArticles();
    
    // 埋点：搜索
    if (query.trim()) {
      TrackService.track('article_search', { query });
    }
  },
  
  // 获取文章评论
  fetchComments: async (articleId, page = 1, pageSize = 20) => {
    set({ commentLoading: true, commentError: null });
    
    try {
      let result;
      
      if (useMockData) {
        // 使用模拟数据
        await mockDelay(); // 模拟网络延迟
        result = getMockArticleComments(articleId, page, pageSize);
      } else {
        // 使用真实API
        result = await articleAPI.getArticleComments(articleId, page, pageSize);
      }
      
      set({
        comments: result.list,
        totalComments: result.total,
        commentLoading: false
      });
      
      // 埋点：评论列表加载完成
      TrackService.track('comments_loaded', {
        article_id: articleId,
        page,
        pageSize,
        total: result.total
      });
      
    } catch (error) {
      console.error('Failed to fetch comments:', error);
      set({
        commentError: '获取评论失败，请稍后重试',
        commentLoading: false
      });
      
      // 埋点：评论列表加载失败
      TrackService.track('comments_load_failed', {
        article_id: articleId,
        error: error instanceof Error ? error.message : 'Unknown error'
      });
    }
  },
  
  // 添加评论
  addComment: async (articleId, content, parentId) => {
    set({ commentLoading: true, commentError: null });
    
    try {
      let newComment;
      
      if (useMockData) {
        // 使用模拟数据
        await mockDelay(); // 模拟网络延迟
        newComment = addMockComment(articleId, content, parentId);
      } else {
        // 使用真实API
        newComment = await articleAPI.addComment(articleId, content, parentId);
      }
      
      // 更新本地状态
      set(state => ({
        comments: [newComment, ...state.comments],
        totalComments: state.totalComments + 1,
        commentLoading: false,
        // 更新文章的评论数
        articleDetail: state.articleDetail?.id === articleId 
          ? { ...state.articleDetail, commentCount: (state.articleDetail.commentCount || 0) + 1 }
          : state.articleDetail,
        articles: state.articles.map(article => 
          article.id === articleId 
            ? { ...article, commentCount: (article.commentCount || 0) + 1 }
            : article
        )
      }));
      
      // 埋点：评论添加成功
      TrackService.track('comment_added', {
        article_id: articleId,
        parent_id: parentId
      });
      
    } catch (error) {
      console.error('Failed to add comment:', error);
      set({
        commentError: '发表评论失败，请稍后重试',
        commentLoading: false
      });
      
      // 埋点：评论添加失败
      TrackService.track('comment_add_failed', {
        article_id: articleId,
        parent_id: parentId,
        error: error instanceof Error ? error.message : 'Unknown error'
      });
    }
  },
  
  // 重置所有筛选条件
  resetFilters: () => {
    set({
      selectedCategory: null,
      selectedTag: null,
      searchQuery: '',
      currentPage: 1
    });
    
    // 重新获取文章列表
    get().fetchArticles();
  }
}));
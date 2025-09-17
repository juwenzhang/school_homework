import type { EventParams } from '../types';

/**
 * 埋点服务 - 用于跟踪用户行为和应用事件
 */
class TrackService {
  /**
   * 跟踪事件
   * @param eventName 事件名称
   * @param eventParams 事件参数
   */
  static track(eventName: string, eventParams?: EventParams): void {
    console.log('Blog app track event:', eventName, eventParams);
    
    // 实际环境中会发送到埋点服务器
    try {
      const win = window as unknown as { 
        analytics?: { 
          track: (name: string, params?: EventParams) => void 
        } 
      };
      
      if (win.analytics) {
        win.analytics.track(eventName, eventParams);
      }
    } catch (error) {
      console.error('Error tracking event in blog app:', error);
    }
  }

  /**
   * 跟踪页面访问
   * @param path 页面路径
   */
  static trackPageView(path: string): void {
    this.track('page_view', {
      path,
      timestamp: new Date().toISOString(),
    });
  }

  /**
   * 跟踪文章点击
   * @param articleId 文章ID
   * @param articleTitle 文章标题
   */
  static trackArticleClick(articleId: string, articleTitle: string): void {
    this.track('article_click', {
      article_id: articleId,
      article_title: articleTitle,
    });
  }

  /**
   * 跟踪文章点赞
   * @param articleId 文章ID
   * @param articleTitle 文章标题
   */
  static trackArticleLike(articleId: string, articleTitle: string): void {
    this.track('article_like', {
      article_id: articleId,
      article_title: articleTitle,
    });
  }

  /**
   * 跟踪文章评论
   * @param articleId 文章ID
   * @param articleTitle 文章标题
   */
  static trackArticleComment(articleId: string, articleTitle: string): void {
    this.track('article_comment', {
      article_id: articleId,
      article_title: articleTitle,
    });
  }

  /**
   * 跟踪文章详情页访问
   * @param articleId 文章ID
   */
  static trackArticleDetailView(articleId: string): void {
    this.track('article_detail_view', {
      article_id: articleId,
    });
  }

  /**
   * 跟踪搜索
   * @param query 搜索查询
   */
  static trackSearch(query: string): void {
    this.track('search', {
      query,
    });
  }

  /**
   * 跟踪分类选择
   * @param category 分类名称
   */
  static trackCategorySelect(category: string): void {
    this.track('category_select', {
      category,
    });
  }

  /**
   * 跟踪标签选择
   * @param tag 标签名称
   */
  static trackTagSelect(tag: string): void {
    this.track('tag_select', {
      tag,
    });
  }

  /**
   * 跟踪页面切换
   * @param page 当前页面
   * @param pageSize 页面大小
   */
  static trackPageChange(page: number, pageSize: number): void {
    this.track('page_change', {
      page,
      page_size: pageSize,
    });
  }

  /**
   * 跟踪应用初始化
   * @param environment 环境
   */
  static trackAppInit(environment: string): void {
    this.track('app_init', {
      app: 'blog-app',
      timestamp: new Date().toISOString(),
      environment
    });
  }
}

export default TrackService;
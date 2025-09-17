import type { Article, Category, Tag, Comment } from '../types';

// 模拟文章数据
export const mockArticles: Article[] = [
  {
    id: '1',
    title: 'React 19 新特性解析',
    summary: '本文详细介绍了React 19的新特性，包括自动批处理、并发渲染和Suspense改进等内容。',
    content: '<p>React 19是React团队推出的最新版本，带来了诸多令人兴奋的新特性。</p><h3>自动批处理改进</h3><p>React 19对自动批处理进行了全面改进，现在可以在更多场景下自动合并多次状态更新，提升应用性能。</p><h3>并发渲染优化</h3><p>并发渲染是React 19的核心特性之一，它允许React在不阻塞UI的情况下准备多个版本的UI。</p><h3>Suspense的新能力</h3><p>React 19大大增强了Suspense的能力，使其可以在更多场景下使用，包括数据获取、代码分割等。</p><p>总的来说，React 19为前端开发者提供了更强大、更灵活的工具，可以构建性能更好、用户体验更佳的应用。</p>',
    author: {
      id: '1',
      name: '张三',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhangsan',
    },
    coverImage: 'https://picsum.photos/id/1/600/400',
    category: '前端开发',
    tags: ['React', 'JavaScript', '前端'],
    viewCount: 1234,
    likeCount: 89,
    commentCount: 12,
    publishTime: '2023-01-15T08:30:00Z',
    updateTime: '2023-01-15T08:30:00Z',
  },
  {
    id: '2',
    title: '微前端架构实践与总结',
    summary: '本文分享了在大型项目中采用微前端架构的实践经验和遇到的挑战。',
    content: '<p>随着项目规模的扩大，传统的单页应用架构面临着诸多挑战，微前端架构应运而生。</p><h3>微前端的优势</h3><p>微前端可以将大型应用拆分为多个独立开发、独立部署的小型应用，降低了团队协作的复杂度。</p><h3>实践中的挑战</h3><p>在实际项目中，我们遇到了样式隔离、状态共享、路由集成等诸多挑战，通过使用single-spa等框架，我们成功解决了这些问题。</p><h3>总结与展望</h3><p>微前端架构为大型应用提供了一种有效的解决方案，但也需要根据项目实际情况进行合理的规划和设计。</p>',
    author: {
      id: '2',
      name: '李四',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=lisi',
    },
    coverImage: 'https://picsum.photos/id/2/600/400',
    category: '架构设计',
    tags: ['微前端', '架构', '模块化'],
    viewCount: 890,
    likeCount: 67,
    commentCount: 18,
    publishTime: '2023-01-10T14:20:00Z',
    updateTime: '2023-01-10T14:20:00Z',
  },
  {
    id: '3',
    title: 'TypeScript高级类型技巧',
    summary: '深入探讨TypeScript中的高级类型系统，帮助你写出更健壮的代码。',
    content: '<p>TypeScript的类型系统非常强大，掌握高级类型技巧可以大大提升代码质量和开发效率。</p><h3>条件类型</h3><p>条件类型允许我们根据条件选择不同的类型，是实现类型多态的重要工具。</p><pre><code>type IsString<T> = T extends string ? true : false;</code></pre><h3>映射类型</h3><p>映射类型可以基于现有类型创建新的类型，非常适合批量处理类型的属性。</p><pre><code>type Readonly<T> = { readonly [P in keyof T]: T[P] };</code></pre><h3>类型守卫</h3><p>类型守卫可以在运行时检查类型，帮助TypeScript推断更精确的类型信息。</p><p>掌握这些高级类型技巧，可以让你的TypeScript代码更加健壮、可维护。</p>',
    author: {
      id: '3',
      name: '王五',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=wangwu',
    },
    coverImage: 'https://picsum.photos/id/3/600/400',
    category: '前端开发',
    tags: ['TypeScript', '类型系统', 'JavaScript'],
    viewCount: 1567,
    likeCount: 103,
    commentCount: 34,
    publishTime: '2023-01-05T10:15:00Z',
    updateTime: '2023-01-05T10:15:00Z',
  },
  {
    id: '4',
    title: '现代CSS布局技巧',
    summary: '介绍Flexbox和Grid布局的高级技巧，让你的页面布局更加灵活和响应式。',
    content: '<p>CSS布局是前端开发的基础，现代CSS提供了Flexbox和Grid等强大的布局工具。</p><h3>Flexbox高级技巧</h3><p>Flexbox适合一维布局，通过合理设置flex属性，可以轻松实现各种复杂的水平或垂直布局。</p><h3>Grid布局详解</h3><p>Grid布局是二维的，可以同时处理行和列，非常适合复杂的页面布局。</p><h3>响应式设计策略</h3><p>结合媒体查询和现代布局技术，可以创建在不同设备上都有良好表现的响应式页面。</p>',
    author: {
      id: '1',
      name: '张三',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhangsan',
    },
    coverImage: 'https://picsum.photos/id/4/600/400',
    category: '前端开发',
    tags: ['CSS', 'Flexbox', 'Grid', '响应式'],
    viewCount: 987,
    likeCount: 76,
    commentCount: 15,
    publishTime: '2023-01-01T16:45:00Z',
    updateTime: '2023-01-01T16:45:00Z',
  },
  {
    id: '5',
    title: 'Node.js性能优化实践',
    summary: '分享Node.js应用性能优化的实用技巧，提升服务响应速度和吞吐量。',
    content: '<p>Node.js作为服务器端JavaScript运行时，性能优化是非常重要的课题。</p><h3>异步编程优化</h3><p>合理使用Promise和async/await，避免回调地狱，提高代码可读性和性能。</p><h3>内存管理</h3><p>了解Node.js的内存模型，避免内存泄漏，优化内存使用效率。</p><h3>集群模式</h3><p>使用Node.js的集群模块，充分利用多核CPU，提高应用的并发处理能力。</p><h3>缓存策略</h3><p>合理使用各种缓存技术，减少数据库查询和计算开销。</p>',
    author: {
      id: '4',
      name: '赵六',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhaoliu',
    },
    coverImage: 'https://picsum.photos/id/5/600/400',
    category: '后端开发',
    tags: ['Node.js', '性能优化', 'JavaScript'],
    viewCount: 1345,
    likeCount: 92,
    commentCount: 23,
    publishTime: '2022-12-28T09:10:00Z',
    updateTime: '2022-12-28T09:10:00Z',
  }
];

// 模拟分类数据
export const mockCategories: Category[] = [
  { id: '1', name: '前端开发', count: 56 },
  { id: '2', name: '架构设计', count: 23 },
  { id: '3', name: '后端开发', count: 45 },
  { id: '4', name: '数据库', count: 18 },
  { id: '5', name: 'DevOps', count: 32 },
];

// 模拟标签数据
export const mockTags: Tag[] = [
  { id: '1', name: 'React', count: 128 },
  { id: '2', name: 'TypeScript', count: 95 },
  { id: '3', name: '微前端', count: 42 },
  { id: '4', name: '架构', count: 67 },
  { id: '5', name: 'JavaScript', count: 156 },
  { id: '6', name: 'Vue', count: 89 },
  { id: '7', name: 'Node.js', count: 78 },
  { id: '8', name: 'Docker', count: 54 },
];

/**
 * 获取模拟的文章列表
 * @param page 页码
 * @param pageSize 每页数量
 * @param category 分类（可选）
 * @param tag 标签（可选）
 * @param search 搜索关键词（可选）
 * @returns 文章列表和总数
 */
export const getMockArticles = (
  page: number = 1,
  pageSize: number = 10,
  category?: string,
  tag?: string,
  search?: string
): { list: Article[]; total: number } => {
  let filteredArticles = [...mockArticles];
  
  // 按分类过滤
  if (category) {
    filteredArticles = filteredArticles.filter(article => article.category === category);
  }
  
  // 按标签过滤
  if (tag) {
    filteredArticles = filteredArticles.filter(article => article.tags.includes(tag));
  }
  
  // 按搜索关键词过滤
  if (search) {
    const searchLower = search.toLowerCase();
    filteredArticles = filteredArticles.filter(
      article => 
        article.title.toLowerCase().includes(searchLower) ||
        article.summary.toLowerCase().includes(searchLower) ||
        article.content.toLowerCase().includes(searchLower)
    );
  }
  
  // 分页
  const startIndex = (page - 1) * pageSize;
  const endIndex = startIndex + pageSize;
  const paginatedArticles = filteredArticles.slice(startIndex, endIndex);
  
  return {
    list: paginatedArticles,
    total: filteredArticles.length
  };
};

/**
 * 获取模拟的文章详情
 * @param id 文章ID
 * @returns 文章详情
 */
export const getMockArticleDetail = (id: string): Article | undefined => {
  return mockArticles.find(article => article.id === id);
};

/**
 * 模拟评论数据
 */
export const mockComments: Comment[] = [
  {
    id: '1',
    articleId: '1',
    content: '感谢分享，这篇文章对我帮助很大！React 19的新特性确实很令人期待。',
    author: {
      id: '5',
      name: '小明',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaoming'
    },
    publishTime: '2023-01-15T09:30:00Z',
    likes: 5
  },
  {
    id: '2',
    articleId: '1',
    content: '我想知道React 19的自动批处理改进具体体现在哪些场景下？',
    author: {
      id: '6',
      name: '小红',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaohong'
    },
    publishTime: '2023-01-15T10:15:00Z',
    likes: 3
  },
  {
    id: '3',
    articleId: '1',
    content: '非常详细的解析，期待更多这样的技术分享！',
    author: {
      id: '7',
      name: '小刚',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaogang'
    },
    publishTime: '2023-01-15T11:00:00Z',
    likes: 2
  },
  {
    id: '4',
    articleId: '2',
    content: '我们团队也在尝试微前端架构，遇到了一些挑战，这篇文章提供了很好的思路。',
    author: {
      id: '8',
      name: '小李',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaoli'
    },
    publishTime: '2023-01-10T15:30:00Z',
    likes: 8
  },
  {
    id: '5',
    articleId: '3',
    content: 'TypeScript的高级类型确实很强大，这篇文章解释得很清楚！',
    author: {
      id: '9',
      name: '小张',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaozhang'
    },
    publishTime: '2023-01-05T11:45:00Z',
    likes: 6
  }
];

/**
 * 获取模拟的文章评论
 * @param articleId 文章ID
 * @param page 页码
 * @param pageSize 每页数量
 * @returns 评论列表和总数
 */
export const getMockArticleComments = (
  articleId: string,
  page: number = 1,
  pageSize: number = 20
): { list: Comment[]; total: number } => {
  // 筛选指定文章的评论
  const articleComments = mockComments.filter(comment => comment.articleId === articleId);
  
  // 分页
  const startIndex = (page - 1) * pageSize;
  const endIndex = startIndex + pageSize;
  const paginatedComments = articleComments.slice(startIndex, endIndex);
  
  return {
    list: paginatedComments,
    total: articleComments.length
  };
};

/**
 * 添加模拟评论
 * @param articleId 文章ID
 * @param content 评论内容
 * @param parentId 父评论ID（可选）
 * @returns 添加的评论
 */
export const addMockComment = (
  articleId: string,
  content: string,
  parentId?: string
): Comment => {
  const newComment: Comment = {
    id: Date.now().toString(),
    articleId,
    content,
    author: {
      id: 'current_user',
      name: '当前用户',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=currentuser'
    },
    publishTime: new Date().toISOString(),
    parentId,
    likes: 0
  };
  
  // 添加到模拟数据中
  mockComments.unshift(newComment);
  
  return newComment;
};

/**
 * 模拟API延迟
 * @param ms 延迟毫秒数
 * @returns Promise
 */
export const mockDelay = (ms: number = 300): Promise<void> => {
  return new Promise(resolve => setTimeout(resolve, ms));
}
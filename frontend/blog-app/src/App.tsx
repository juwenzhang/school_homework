import React, { useState, useEffect } from 'react';
import { Routes, Route, Link, useLocation, useParams } from 'react-router-dom';
import { Layout, Avatar, Button, Space, Card, Input, List, Tag, Divider } from 'antd';
import { UserOutlined, SearchOutlined, EyeOutlined, HeartOutlined, MessageOutlined, ShareAltOutlined, BookOutlined, TagsOutlined, TagOutlined, ArrowUpOutlined } from '@ant-design/icons';
import styled from '@emotion/styled';
import './App.css';

// 定义接口
interface AppProps {
  base: string;
}

interface Article {
  id: string;
  title: string;
  summary: string;
  content: string;
  author: {
    id: string;
    name: string;
    avatar: string;
  };
  coverImage?: string;
  category: string;
  tags: string[];
  viewCount: number;
  likeCount: number;
  commentCount: number;
  publishTime: string;
  updateTime: string;
}

interface Category {
  id: string;
  name: string;
  count: number;
}

interface Tag {
  id: string;
  name: string;
  count: number;
}

// 全局样式组件
const BlogLayout = styled.div`
  min-height: 100vh;
  display: flex;
  flex-direction: column;
`;

const AppHeader = styled(Layout.Header)`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
  z-index: 10;
`;

const AppContent = styled(Layout.Content)`
  padding: 24px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 64px);
`;

const MainContainer = styled.div`
  display: flex;
  gap: 24px;
  max-width: 1200px;
  margin: 0 auto;
`;

const ArticleListContainer = styled.div`
  flex: 1;
`;

const Sidebar = styled.div`
  width: 300px;
  flex-shrink: 0;
`;

// 模拟数据
const mockArticles: Article[] = [
  {
    id: '1',
    title: 'React 19 新特性解析',
    summary: '本文详细介绍了React 19的新特性，包括自动批处理、并发渲染和Suspense改进等内容。',
    content: 'React 19是React团队推出的最新版本...',
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
    commentCount: 23,
    publishTime: '2023-01-15T08:30:00Z',
    updateTime: '2023-01-15T08:30:00Z',
  },
  {
    id: '2',
    title: '微前端架构实践与总结',
    summary: '本文分享了在大型项目中采用微前端架构的实践经验和遇到的挑战。',
    content: '随着项目规模的扩大，传统的单页应用架构面临着诸多挑战...',
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
    content: 'TypeScript的类型系统非常强大，掌握高级类型技巧可以大大提升代码质量...',
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
];

const mockCategories: Category[] = [
  { id: '1', name: '前端开发', count: 56 },
  { id: '2', name: '架构设计', count: 23 },
  { id: '3', name: '后端开发', count: 45 },
  { id: '4', name: '数据库', count: 18 },
  { id: '5', name: 'DevOps', count: 32 },
];

const mockTags: Tag[] = [
  { id: '1', name: 'React', count: 128 },
  { id: '2', name: 'TypeScript', count: 95 },
  { id: '3', name: '微前端', count: 42 },
  { id: '4', name: '架构', count: 67 },
  { id: '5', name: 'JavaScript', count: 156 },
  { id: '6', name: 'Vue', count: 89 },
  { id: '7', name: 'Node.js', count: 78 },
  { id: '8', name: 'Docker', count: 54 },
];

// 埋点服务
const trackEvent = (eventName: string, eventParams?: Record<string, unknown>): void => {
  console.log('Blog app track event:', eventName, eventParams);
  // 实际环境中会发送到埋点服务器
  try {
    const win = window as unknown as { analytics?: { track: (name: string, params?: Record<string, unknown>) => void } };
    if (win.analytics) {
      win.analytics.track(eventName, eventParams);
    }
  } catch (error) {
    console.error('Error tracking event in blog app:', error);
  }
};

// 文章卡片组件
const ArticleCard: React.FC<{ article: Article }> = ({ article }) => {
  const handleArticleClick = () => {
    trackEvent('article_click', {
      article_id: article.id,
      article_title: article.title,
    });
  };

  const handleLikeClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    trackEvent('article_like', {
      article_id: article.id,
      article_title: article.title,
    });
  };

  const handleCommentClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    trackEvent('article_comment', {
      article_id: article.id,
      article_title: article.title,
    });
  };

  return (
    <Card
      hoverable
      className="article-card"
      cover={article.coverImage ? <img alt={article.title} src={article.coverImage} /> : null}
      onClick={handleArticleClick}
    >
      <Card.Meta
        avatar={<Avatar src={article.author.avatar} />}
        title={<Link to={`/blog/article/${article.id}`}>{article.title}</Link>}
        description={article.summary}
      />
      <div className="article-meta">
        <span className="author-name">{article.author.name}</span>
        <span className="publish-time">{new Date(article.publishTime).toLocaleDateString()}</span>
        <Tag color="blue" className="category-tag">{article.category}</Tag>
      </div>
      <div className="article-tags">
        {article.tags.map(tag => (
          <Tag key={tag} className="article-tag">{tag}</Tag>
        ))}
      </div>
      <div className="article-stats">
        <span className="stat-item">
          <EyeOutlined />
          {article.viewCount}
        </span>
        <span className="stat-item" onClick={handleLikeClick}>
          <HeartOutlined />
          {article.likeCount}
        </span>
        <span className="stat-item" onClick={handleCommentClick}>
          <MessageOutlined />
          {article.commentCount}
        </span>
        <span className="stat-item">
          <ShareAltOutlined />
        </span>
        <span className="stat-item">
          <BookOutlined />
        </span>
      </div>
    </Card>
  );
};

// 文章详情组件
const ArticleDetail: React.FC = () => {
  const { id } = useParams();
  const [article, setArticle] = useState<Article | null>(null);

  useEffect(() => {
    // 模拟获取文章详情
    const foundArticle = mockArticles.find(a => a.id === id);
    setArticle(foundArticle || null);

    // 埋点：文章详情页访问
    trackEvent('article_detail_view', {
      article_id: id,
    });
  }, [id]);

  if (!article) {
    return <div className="article-detail">文章不存在</div>;
  }

  return (
    <div className="article-detail">
      <h1>{article.title}</h1>
      <div className="article-meta">
        <span className="author-info">
          <Avatar src={article.author.avatar} /> {article.author.name}
        </span>
        <span className="publish-time">{new Date(article.publishTime).toLocaleString()}</span>
      </div>
      <div className="article-category-tags">
        <Tag color="blue"><TagsOutlined /> {article.category}</Tag>
        {article.tags.map(tag => (
          <Tag key={tag}><TagOutlined /> {tag}</Tag>
        ))}
      </div>
      <Divider />
      <div className="article-content">
        {article.coverImage && <img alt={article.title} src={article.coverImage} className="article-cover" />}
        <div dangerouslySetInnerHTML={{ __html: article.content }} />
      </div>
      <Divider />
      <div className="article-stats">
        <span className="stat-item">
          <EyeOutlined /> 浏览 {article.viewCount}
        </span>
        <span className="stat-item">
          <HeartOutlined /> 点赞 {article.likeCount}
        </span>
        <span className="stat-item">
          <MessageOutlined /> 评论 {article.commentCount}
        </span>
      </div>
    </div>
  );
};

// 博客主应用组件
const App: React.FC<AppProps> = (_props) => {
  const location = useLocation();
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  const [selectedTag, setSelectedTag] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 10;

  // 处理搜索
  const handleSearch = () => {
    trackEvent('search', {
      query: searchQuery,
    });
    // 实际环境中会触发搜索逻辑
  };

  // 处理分类选择
  const handleCategoryClick = (category: string) => {
    setSelectedCategory(category);
    setSelectedTag(null);
    setCurrentPage(1);
    trackEvent('category_select', {
      category,
    });
  };

  // 处理标签选择
  const handleTagClick = (tag: string) => {
    setSelectedTag(tag);
    setSelectedCategory(null);
    setCurrentPage(1);
    trackEvent('tag_select', {
      tag,
    });
  };

  // 处理页面变化
  const handlePageChange = (page: number) => {
    setCurrentPage(page);
    trackEvent('page_change', {
      page,
      page_size: pageSize,
    });
  };

  // 埋点：页面访问
  useEffect(() => {
    trackEvent('page_view', {
      path: location.pathname,
      timestamp: new Date().toISOString(),
    });
  }, [location]);

  return (
    <BlogLayout>
      <AppHeader>
        <div className="logo">
          <h1>Blog Platform</h1>
        </div>
        <div className="user-actions">
          <Space>
            <Button type="text" icon={<SearchOutlined />} />
            <Button type="primary">登录</Button>
          </Space>
        </div>
      </AppHeader>
      
      <AppContent>
        <MainContainer>
          <ArticleListContainer>
            {/* 搜索框 */}
            <div className="search-container">
              <Input
                placeholder="搜索文章..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                onPressEnter={handleSearch}
                addonAfter={
                  <Button type="primary" onClick={handleSearch}>
                    <SearchOutlined />
                    搜索
                  </Button>
                }
                size="large"
              />
            </div>
            
            {/* 文章列表 */}
            <List
              grid={{ gutter: 16, column: 1 }}
              dataSource={mockArticles}
              renderItem={(article) => (
                <List.Item>
                  <ArticleCard article={article} />
                </List.Item>
              )}
              pagination={{
                current: currentPage,
                pageSize: pageSize,
                total: mockArticles.length,
                onChange: handlePageChange,
                showSizeChanger: false,
                showQuickJumper: true,
                showTotal: (total) => `共 ${total} 篇文章`,
              }}
            />
          </ArticleListContainer>
          
          <Sidebar>
            {/* 用户信息卡片 */}
            <Card className="user-card">
              <div className="user-info">
                <Avatar size={64} icon={<UserOutlined />} className="user-avatar" />
                <div className="user-details">
                  <h3>访客</h3>
                  <p>欢迎来到博客平台</p>
                </div>
              </div>
              <Button type="primary" block>登录</Button>
            </Card>
            
            {/* 分类列表 */}
            <Card title="文章分类" className="category-card">
              <List
                dataSource={mockCategories}
                renderItem={(category) => (
                  <List.Item
                    className={selectedCategory === category.name ? 'selected' : ''}
                    onClick={() => handleCategoryClick(category.name)}
                  >
                    <div className="category-item">
                      <span className="category-name">{category.name}</span>
                      <span className="category-count">{category.count}</span>
                    </div>
                  </List.Item>
                )}
              />
            </Card>
            
            {/* 标签云 */}
            <Card title="热门标签" className="tag-card">
              <div className="tag-cloud">
                {mockTags.map(tag => (
                  <Tag
                    key={tag.id}
                    className={`tag-item ${selectedTag === tag.name ? 'selected' : ''}`}
                    onClick={() => handleTagClick(tag.name)}
                  >
                    {tag.name} ({tag.count})
                  </Tag>
                ))}
              </div>
            </Card>
          </Sidebar>
        </MainContainer>
      </AppContent>
      
      {/* 返回顶部按钮 */}
      <Button
        type="primary"
        shape="circle"
        icon={<ArrowUpOutlined />}
        className="back-to-top"
        onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}
      />
      
      {/* 路由配置 */}
      <Routes>
        <Route path="/" element={<div className="blog-home">博客首页</div>} />
        <Route path="/article/:id" element={<ArticleDetail />} />
        <Route path="/category/:categoryId" element={<div className="category-page">分类页面</div>} />
        <Route path="/tag/:tagId" element={<div className="tag-page">标签页面</div>} />
        <Route path="/author/:authorId" element={<div className="author-page">作者页面</div>} />
      </Routes>
    </BlogLayout>
  );
};

export default App;

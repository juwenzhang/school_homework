import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Avatar, Typography, List, Card, Empty } from 'antd';
import styled from '@emotion/styled';
import { userAPI } from '../services/api';
import { useArticleStore } from '../store/articleStore';
import type { User } from '../types';
import ArticleCard from '../components/article/ArticleCard';
import TrackService from '../utils/track';
import { mockDelay } from '../services/mockData';

const { Title, Paragraph, Text } = Typography;

interface AuthorPageParams {
  authorId: string;
  [key: string]: string | undefined;
}

// 判断是否使用模拟数据
const useMockData = process.env.NODE_ENV === 'development';

// 模拟作者数据
const mockAuthors: Record<string, User> = {
  '1': {
    id: '1',
    name: '张三',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhangsan',
    email: 'zhangsan@example.com',
    bio: '前端开发专家，React 爱好者，热衷于分享前端技术和最佳实践。',
    role: 'author'
  },
  '2': {
    id: '2',
    name: '李四',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=lisi',
    email: 'lisi@example.com',
    bio: '架构师，专注于微前端、微服务等分布式架构的设计和实践。',
    role: 'author'
  },
  '3': {
    id: '3',
    name: '王五',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=wangwu',
    email: 'wangwu@example.com',
    bio: 'TypeScript 布道师，热爱探索类型系统的奥秘。',
    role: 'author'
  },
  '4': {
    id: '4',
    name: '赵六',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhaoliu',
    email: 'zhaoliu@example.com',
    bio: '全栈开发者，Node.js 专家，擅长性能优化和系统设计。',
    role: 'author'
  }
};

const AuthorPageContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;

  .author-profile {
    display: flex;
    align-items: center;
    gap: 24px;
    margin-bottom: 32px;
    padding: 24px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  }

  .author-avatar {
    flex-shrink: 0;
  }

  .author-info {
    flex: 1;
  }

  .author-info h2 {
    margin: 0 0 8px 0;
  }

  .author-bio {
    color: #666;
    line-height: 1.6;
  }

  .author-stats {
    display: flex;
    gap: 32px;
    margin-top: 16px;
  }

  .stat-item {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .stat-number {
    font-weight: 600;
    font-size: 18px;
    color: #1890ff;
  }

  .author-articles {
    max-width: 800px;
  }

  .articles-title {
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid #f0f0f0;
  }
`;

const AuthorPage: React.FC = () => {
  const { authorId } = useParams<AuthorPageParams>();
  const { articles, totalArticles, loading, error, currentPage, pageSize, fetchArticles } = useArticleStore();
  const [author, setAuthor] = useState<User | null>(null);
  const [authorLoading, setAuthorLoading] = useState(true);
  const [authorError, setAuthorError] = useState<string | null>(null);

  // 获取作者信息
  const fetchAuthorInfo = async () => {
    if (!authorId) return;
    
    setAuthorLoading(true);
    setAuthorError(null);
    
    try {
      let authorData;
      
      if (useMockData) {
        // 使用模拟数据
        await mockDelay(); // 模拟网络延迟
        authorData = mockAuthors[authorId];
        
        if (!authorData) {
          throw new Error('Author not found');
        }
      } else {
        // 使用真实API
        authorData = await userAPI.getUserInfo(authorId);
      }
      
      setAuthor(authorData);
      
    } catch (error) {
      console.error('Failed to fetch author info:', error);
      setAuthorError('获取作者信息失败');
      
      // 埋点：作者信息加载失败
      TrackService.track('author_info_load_failed', {
        author_id: authorId,
        error: error instanceof Error ? error.message : 'Unknown error'
      });
    } finally {
      setAuthorLoading(false);
    }
  };

  // 初始化时获取作者信息和文章列表
  useEffect(() => {
    fetchAuthorInfo();
  }, [authorId]);

  // 当作者信息加载完成后，获取该作者的文章
  useEffect(() => {
    if (author && authorId) {
      fetchArticles(1, 10);
      TrackService.track('author_page_view', {
        author_id: authorId,
        author_name: author.name
      });
    }
  }, [author, authorId, fetchArticles]);

  // 处理页面变化
  const handlePageChange = (page: number) => {
    fetchArticles(page);
    TrackService.trackPageChange(page, pageSize);
  };



  if (authorLoading) {
    return (
      <AuthorPageContainer>
        <Card loading />
      </AuthorPageContainer>
    );
  }

  if (authorError || !author) {
    return (
      <AuthorPageContainer>
        <Card>
          <Empty description={authorError || '作者不存在'} />
        </Card>
      </AuthorPageContainer>
    );
  }

  return (
    <AuthorPageContainer>
      <div className="author-profile">
        <Avatar 
          size={128} 
          src={author.avatar} 
          className="author-avatar"
        />
        
        <div className="author-info">
          <Title level={2}>{author.name}</Title>
          <Paragraph className="author-bio">{author.bio}</Paragraph>
          
          <div className="author-stats">
            <div className="stat-item">
              <span className="stat-number">{totalArticles}</span>
              <Text>文章</Text>
            </div>
            <div className="stat-item">
              <span className="stat-number">{author.role}</span>
              <Text>角色</Text>
            </div>
          </div>
        </div>
      </div>

      <div className="author-articles">
        <Title level={3} className="articles-title">
          {author.name} 的文章
        </Title>
        
        <List
          grid={{ gutter: 16, column: 1 }}
          dataSource={articles}
          renderItem={(article) => (
            <List.Item>
              <ArticleCard article={article} />
            </List.Item>
          )}
          pagination={{
            current: currentPage,
            pageSize: pageSize,
            total: totalArticles,
            onChange: handlePageChange,
            showSizeChanger: false,
            showQuickJumper: true,
            showTotal: (total) => `共 ${total} 篇文章`,
          }}
          loading={loading}
          locale={{
            emptyText: error || '暂无文章',
          }}
        />
      </div>
    </AuthorPageContainer>
  );
};

export default AuthorPage;
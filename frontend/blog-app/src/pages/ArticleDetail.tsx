import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Card, Avatar, Tag, Divider, List, Input, Button } from 'antd';
import { EyeOutlined, HeartOutlined, MessageOutlined, TagsOutlined, TagOutlined } from '@ant-design/icons';
import styled from '@emotion/styled';
import { useArticleStore } from '../store/articleStore';
import TrackService from '../utils/track';
import { formatDate } from '../utils/common';
import type { Comment } from '../types';

// 定义样式容器
const ArticleDetailContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;

  .article-detail {
    background: #fff;
    padding: 24px;
    border-radius: 8px;
  }

  .article-detail h1 {
    margin: 0 0 16px 0;
    font-size: 28px;
    font-weight: 600;
    color: #333;
  }

  .article-meta {
    display: flex;
    align-items: center;
    gap: 16px;
    margin-bottom: 16px;
    font-size: 14px;
    color: #666;
  }

  .author-info {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .publish-time {
    color: #999;
  }

  .article-category-tags {
    display: flex;
    gap: 12px;
    margin-bottom: 24px;
  }

  .article-cover {
    width: 100%;
    max-height: 400px;
    object-fit: cover;
    border-radius: 8px;
    margin-bottom: 24px;
  }

  .article-content {
    line-height: 1.8;
    font-size: 16px;
    color: #333;

    p {
      margin: 0 0 16px 0;
    }

    h2, h3, h4, h5, h6 {
      margin: 24px 0 16px 0;
      color: #222;
    }

    img {
      max-width: 100%;
      height: auto;
      margin: 16px 0;
    }

    pre {
      background: #f5f5f5;
      padding: 16px;
      border-radius: 4px;
      overflow-x: auto;
      margin: 16px 0;
    }

    code {
      background: #f5f5f5;
      padding: 2px 6px;
      border-radius: 4px;
      font-family: 'Courier New', Courier, monospace;
    }

    blockquote {
      border-left: 4px solid #1890ff;
      padding-left: 16px;
      color: #666;
      margin: 16px 0;
    }
  }

  .article-stats {
    display: flex;
    align-items: center;
    gap: 24px;
    margin-top: 24px;
    color: #666;
    font-size: 16px;
  }

  .stat-item {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    transition: color 0.2s;
    &:hover {
      color: #1890ff;
    }
  }

  .comments-section {
    margin-top: 40px;
    padding-top: 24px;
    border-top: 1px solid #f0f0f0;
  }

  .comments-section h3 {
    margin: 0 0 24px 0;
    font-size: 20px;
  }

  .comment-form {
    margin-bottom: 32px;
  }

  .comment-input {
    margin-bottom: 16px;
  }

  .comment-item {
    padding: 16px 0;
    border-bottom: 1px solid #f0f0f0;
  }

  .comment-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
  }

  .comment-author-info {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .comment-author-name {
    font-weight: 500;
    color: #333;
  }

  .comment-time {
    font-size: 12px;
    color: #999;
  }

  .comment-content {
    margin: 8px 0;
    color: #333;
    line-height: 1.6;
  }

  .comment-actions {
    display: flex;
    align-items: center;
    gap: 16px;
    font-size: 14px;
    color: #666;
  }

  .comment-action {
    display: flex;
    align-items: center;
    gap: 4px;
    cursor: pointer;
    transition: color 0.2s;
    &:hover {
      color: #1890ff;
    }
  }
`;

function ArticleDetail() {
  const { articleId } = useParams<{ articleId: string }>();
  const { 
    articleDetail, 
    loading, 
    error, 
    comments, 
    totalComments, 
    commentLoading, 
    commentError, 
    fetchArticleDetail, 
    likeArticle, 
    fetchComments, 
    addComment 
  } = useArticleStore();
  const [commentContent, setCommentContent] = useState('');

  // 初始化时获取文章详情
  useEffect(() => {
    if (articleId) {
      fetchArticleDetail(articleId);
      TrackService.trackArticleDetailView(articleId);
    }
  }, [articleId, fetchArticleDetail]);

  // 获取评论列表
  useEffect(() => {
    if (articleId) {
      fetchComments(articleId);
    }
  }, [articleId, fetchComments]);

  // 处理评论提交
  const handleCommentSubmit = () => {
    if (!commentContent.trim() || !articleId) return;
    
    addComment(articleId, commentContent.trim());
    setCommentContent(''); // 清空输入框
    
    // 埋点：用户提交评论
    TrackService.trackArticleComment(articleId, articleDetail?.title || '');
  };

  // 处理评论输入变化
  const handleCommentChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setCommentContent(e.target.value);
  };

  // 处理点赞
  const handleLikeClick = () => {
    if (articleId && articleDetail) {
      likeArticle(articleId);
    }
  };

  if (loading) {
    return (
      <ArticleDetailContainer>
        <Card loading={true} />
      </ArticleDetailContainer>
    );
  }

  if (error || !articleDetail) {
    return (
      <ArticleDetailContainer>
        <Card>
          <p>{error || '文章不存在'}</p>
        </Card>
      </ArticleDetailContainer>
    );
  }

  return (
    <ArticleDetailContainer>
      <div className="article-detail">
        <h1>{articleDetail.title}</h1>
        
        <div className="article-meta">
          <span className="author-info">
            <Avatar src={articleDetail.author.avatar || undefined} /> {articleDetail.author.name}
          </span>
          <span className="publish-time">{formatDate(articleDetail.publishTime, 'YYYY-MM-DD HH:mm')}</span>
        </div>
        
        <div className="article-category-tags">
          <Tag color="blue"><TagsOutlined /> {articleDetail.category}</Tag>
          {articleDetail.tags?.map(tag => (
            <Tag key={tag}><TagOutlined /> {tag}</Tag>
          ))}
        </div>
        
        <Divider />
        
        <div className="article-content">
          {articleDetail.coverImage && (
            <img alt={articleDetail.title} src={articleDetail.coverImage} className="article-cover" />
          )}
          <div dangerouslySetInnerHTML={{ __html: articleDetail.content || '' }} />
        </div>
        
        <Divider />
        
        <div className="article-stats">
          <span className="stat-item">
            <EyeOutlined /> 浏览 {articleDetail.viewCount || 0}
          </span>
          <span className="stat-item" onClick={handleLikeClick}>
            <HeartOutlined /> 点赞 {articleDetail.likeCount || 0}
          </span>
          <span className="stat-item">
            <MessageOutlined /> 评论 {articleDetail.commentCount || 0}
          </span>
        </div>
        
        {/* 评论区域 */}
        <div className="comments-section">
          <h3>评论 ({totalComments})</h3>
          
          {/* 评论输入框 */}
          <div className="comment-form">
            <Input.TextArea
              rows={4}
              placeholder="写下你的评论..."
              value={commentContent}
              onChange={handleCommentChange}
              className="comment-input"
              disabled={commentLoading}
            />
            <Button 
              type="primary" 
              onClick={handleCommentSubmit} 
              loading={commentLoading}
              disabled={!commentContent.trim() || commentLoading}
              style={{ float: 'right' }}
            >
              发表评论
            </Button>
            {commentError && (
              <div style={{ color: 'red', marginTop: 8, clear: 'both' }}>
                {commentError}
              </div>
            )}
          </div>
          
          {/* 评论列表 */}
          <List
            loading={commentLoading}
            dataSource={comments}
            renderItem={(comment: Comment) => (
              <List.Item className="comment-item">
                <div className="comment-header">
                  <div className="comment-author-info">
                    <Avatar src={comment.author.avatar || undefined} />
                    <span className="comment-author-name">{comment.author.name}</span>
                  </div>
                  <span className="comment-time">
                    {formatDate(comment.publishTime, 'YYYY-MM-DD HH:mm')}
                  </span>
                </div>
                <div className="comment-content">
                  {comment.content}
                </div>
                <div className="comment-actions">
                  <span className="comment-action">
                    <HeartOutlined />
                    <span>{comment.likes}</span>
                  </span>
                  <span className="comment-action">回复</span>
                </div>
              </List.Item>
            )}
            locale={{
              emptyText: !commentLoading ? '暂无评论' : ''
            }}
          />
        </div>
      </div>
    </ArticleDetailContainer>
  );
}

export default ArticleDetail;
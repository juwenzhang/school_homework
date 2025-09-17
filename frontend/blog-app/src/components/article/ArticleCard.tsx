import React from 'react';
import { Card, Typography, Tag, Avatar } from 'antd';
import { Link } from 'react-router-dom';
import styled from '@emotion/styled';
import type { Article } from '../../types';
import { formatDate } from '../../utils/common';
import TrackService from '../../utils/track';

const { Text } = Typography;

interface ArticleCardProps {
  article: Article;
  onClick?: (article: Article) => void;
  highlightKeyword?: string;
}

const ArticleCardContainer = styled.div`
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  transition: all 0.3s;
  cursor: pointer;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }

  .search-highlight {
    background-color: #fff76a;
    padding: 0 2px;
    border-radius: 2px;
  }
`;

const ArticleCard: React.FC<ArticleCardProps> = ({ article, onClick, highlightKeyword }) => {
  const handleArticleClick = () => {
    TrackService.trackArticleClick(article.id, article.title);
    if (onClick) {
      onClick(article);
    }
  };

  // 处理关键词高亮
  const renderHighlightedText = (text: string, keyword?: string) => {
    if (!keyword || !text) {
      return text;
    }
    
    return (
      <span dangerouslySetInnerHTML={{
        __html: text.replace(
          new RegExp(`(${keyword})`, 'gi'), 
          '<mark class="search-highlight">$1</mark>'
        ) 
      }} />
    );
  };

  return (
    <ArticleCardContainer onClick={handleArticleClick}>
      <Card
        hoverable
        style={{ border: 'none', boxShadow: 'none' }}
        extra={
          article.category && (
            <Tag color="blue">{article.category}</Tag>
          )
        }
      >
        <Card.Meta
          avatar={<Avatar src={article.author.avatar} />}
          title={
            <Link to={`/blog/article/${article.id}`}>
              {highlightKeyword ? 
                renderHighlightedText(article.title, highlightKeyword) : 
                article.title
              }
            </Link>
          }
          description={
            highlightKeyword ? 
              renderHighlightedText(article.summary, highlightKeyword) : 
              article.summary
          }
        />
        <div style={{
          marginTop: '16px', 
          display: 'flex', 
          justifyContent: 'space-between', 
          alignItems: 'center'
        }}>
          <Text type="secondary">{formatDate(article.publishTime, 'YYYY-MM-DD')}</Text>
          <div style={{ display: 'flex', gap: '8px' }}>
            {article.tags.slice(0, 3).map(tag => (
              <Tag key={tag} color="default">{tag}</Tag>
            ))}
          </div>
        </div>
      </Card>
    </ArticleCardContainer>
  );
};

export default ArticleCard;
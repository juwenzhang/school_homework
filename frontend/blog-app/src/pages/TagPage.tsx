import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { List, Typography } from 'antd';
import styled from '@emotion/styled';
import { useArticleStore } from '../store/articleStore';
import { useCategoryTagStore } from '../store/categoryTagStore';
import ArticleCard from '../components/article/ArticleCard';
import TrackService from '../utils/track';

const { Title } = Typography;

interface TagPageParams {
  [key: string]: string | undefined;
}

const TagPageContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;

  .tag-header {
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid #f0f0f0;
  }

  .article-list {
    max-width: 800px;
  }
`;

const TagPage: React.FC<TagPageParams> = () => {
  const { tagId } = useParams<TagPageParams>();
  const {
    articles,
    totalArticles,
    loading,
    error,
    currentPage,
    pageSize,
    fetchArticles
  } = useArticleStore();
  const { tags, fetchTags } = useCategoryTagStore();

  // 获取当前标签信息
  const currentTag = tags.find(tag => tag.id === tagId);

  // 初始化时获取标签数据
  useEffect(() => {
    fetchTags();
  }, [fetchTags]);

  // 当标签ID变化时，获取该标签下的文章
  useEffect(() => {
    if (tagId) {
      fetchArticles(1, 10, undefined, currentTag?.name);
      TrackService.track('tag_page_view', {
        tag_id: tagId,
        tag_name: currentTag?.name
      });
    }
  }, [tagId, currentTag?.name, fetchArticles]);

  // 处理页面变化
  const handlePageChange = (page: number) => {
    fetchArticles(page);
    TrackService.trackPageChange(page, pageSize);
  };



  return (
    <TagPageContainer>
      <div className="tag-header">
        <Title level={2}>
          {currentTag ? `标签: ${currentTag.name}` : '标签文章'}
        </Title>
        {currentTag && (
          <div className="tag-meta">
            共 {currentTag.count} 篇文章
          </div>
        )}
      </div>

      <div className="article-list">
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
    </TagPageContainer>
  );
};

export default TagPage;
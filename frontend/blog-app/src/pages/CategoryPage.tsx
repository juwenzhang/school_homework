import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { List, Typography } from 'antd';
import styled from '@emotion/styled';
import { useArticleStore } from '../store/articleStore';
import { useCategoryTagStore } from '../store/categoryTagStore';
import ArticleCard from '../components/article/ArticleCard';
import TrackService from '../utils/track';

const { Title } = Typography;

interface CategoryPageParams {
  categoryId: string;
  [key: string]: string | undefined;
}

const CategoryPageContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;

  .category-header {
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid #f0f0f0;
  }

  .article-list {
    max-width: 800px;
  }
`;

const CategoryPage: React.FC = () => {
  const { categoryId } = useParams<CategoryPageParams>();
  const {
    articles,
    totalArticles,
    loading,
    error,
    currentPage,
    pageSize,
    fetchArticles
  } = useArticleStore();
  const { categories, fetchCategories } = useCategoryTagStore();

  // 获取当前分类信息
  const currentCategory = categories.find(cat => cat.id === categoryId);

  // 初始化时获取分类和文章数据
  useEffect(() => {
    fetchCategories();
  }, [fetchCategories]);

  // 当分类ID变化时，获取该分类下的文章
  useEffect(() => {
    if (categoryId) {
      fetchArticles(1, 10, currentCategory?.name);
      TrackService.track('category_page_view', {
        category_id: categoryId,
        category_name: currentCategory?.name
      });
    }
  }, [categoryId, currentCategory?.name, fetchArticles]);

  // 处理页面变化
  const handlePageChange = (page: number) => {
    fetchArticles(page);
    TrackService.trackPageChange(page, pageSize);
  };



  return (
    <CategoryPageContainer>
      <div className="category-header">
        <Title level={2}>
          {currentCategory ? `分类: ${currentCategory.name}` : '分类文章'}
        </Title>
        {currentCategory && (
          <div className="category-meta">
            共 {currentCategory.count} 篇文章
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
    </CategoryPageContainer>
  );
};

export default CategoryPage;